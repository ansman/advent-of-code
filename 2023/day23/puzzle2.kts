#!/usr/bin/env kotlin

import java.util.TreeSet
import kotlin.system.exitProcess
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false

data class XY(val x: Int, val y: Int) {
    override fun toString(): String = "$x,$y"
}

data class Step(val xy: XY, val steps: Int) : Comparable<Step> {
    val x: Int get() = xy.x
    val y: Int get() = xy.y
    override fun compareTo(other: Step): Int = compareValuesBy(this, other, { -it.steps }, { it.y }, { it.x })
}

val grid = generateSequence(::readlnOrNull).toList()
val unvisited = TreeSet<Step>()
val steps = mutableMapOf<XY, Step>()
val h = grid.size
val w = grid[0].length
val visited = HashSet<XY>(h * w)
val start = XY(grid[0].indexOf('.'), 0)
val end = XY(grid.last().indexOf('.'), grid.lastIndex)

grid.forEachIndexed { y, row ->
    row.forEachIndexed { x, c ->
        if (c != '#') {
            val xy = XY(x, y)
            val step = Step(xy, steps = if (xy == start) 0 else -1)
            unvisited.add(step)
            steps[step.xy] = step
        }
    }
}

fun XY.neighbour(dx: Int, dy: Int): XY? {
    val x = x + dx
    val y = y + dy
    return if (x in 0 until w && y in 0 until h && grid[y][x] != '#') {
        XY(x, y)
    } else {
        null
    }
}

fun XY.neighbours(): Sequence<XY> = sequence {
    neighbour(0, -1)?.let { yield(it) }
    neighbour(0, 1)?.let { yield(it) }
    neighbour(-1, 0)?.let { yield(it) }
    neighbour(1, 0)?.let { yield(it) }
}

if (debug) {
    println("Start: $start")
    println("End: $end")
}

fun longestRun(start: XY, visited: MutableSet<XY> = mutableSetOf()): Int {
    if (start == end) {
        if (debug) println("Found end in ${visited.size} steps")
        return visited.size
    }
    if (start in visited) return -1
    visited.add(start)
    return try {
        start
                .neighbours()
                .maxOfOrNull { longestRun(it, visited) }
                ?: -1
    } finally {
        visited.remove(start)
    }
}
println(longestRun(start))

println(startTime.elapsedNow())