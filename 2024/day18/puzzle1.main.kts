#!/usr/bin/env kotlin

import java.util.LinkedList
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

val gridSize = (args.getOrNull(0)?.toInt() ?: 70) + 1
val blockCount = args.getOrNull(1)?.toInt() ?: 1024

val grid = Array(gridSize) { BooleanArray(gridSize) }

data class XY(val x: Int, val y: Int)

data class Path(
    val xy: XY, val steps: Int,
) {
    constructor() : this(XY(0, 0), 0)

    fun step(dx: Int, dy: Int): Path? {
        val x = xy.x + dx
        val y = xy.y + dy
        return if (x in 0 until gridSize && y in 0 until gridSize && !grid[y][x]) {
            Path(XY(x, y), steps + 1)
        } else {
            null
        }
    }
}

generateSequence { readlnOrNull() }
    .take(blockCount)
    .map { l -> l.split(',').map { it.toInt() } }
    .filter { (x, y) -> x in 0 until gridSize && y in 0 until gridSize }
    .forEach { (x, y) -> grid[y][x] = true }

if (debug) {
    grid.forEach { l ->
        println(l.map { if (it) '#' else '.' }.joinToString(""))
    }

}
debug("Computing path with grid size $gridSize and block count $blockCount")

fun runBfs(): Int {
    val target = XY(gridSize - 1, gridSize - 1)
    val queue = LinkedList<Path>()
    val seen = mutableSetOf<XY>()
    queue.add(Path())
    var maxSteps = 0
    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()
        if (seen.add(next.xy)) {
            maxSteps = maxOf(maxSteps, next.steps)
            if (next.xy == target) {
                return next.steps
            }
            next.step(-1, 0)?.let(queue::add)
            next.step(1, 0)?.let(queue::add)
            next.step(0, -1)?.let(queue::add)
            next.step(0, 1)?.let(queue::add)
        }
        if (queue.isEmpty()) {
            debug("Last step was $next")
        }
    }
    throw IllegalArgumentException("Path not found after $maxSteps steps")
}

val steps = runBfs()
println("Minimum steps is $steps")

println("Ran in ${startTime.elapsedNow()}")