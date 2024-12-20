#!/usr/bin/env kotlin

import java.util.*
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

val cheatsAvailable = 20

data class XY(val x: Int, val y: Int)

lateinit var start: XY
lateinit var end: XY

val grid = generateSequence { readlnOrNull() }
    .onEachIndexed { y, l ->
        val s = l.indexOf('S')
        val e = l.indexOf('E')
        if (s != -1) {
            start = XY(s, y)
        }
        if (e != -1) {
            end = XY(e, y)
        }
    }
    .map { l -> BooleanArray(l.length) { l[it] == '#' } }
    .toList()


val stepsToEnd = Array(grid.size) { Array(grid[0].size) { Int.MAX_VALUE } }
val validPlaces = grid.flatMapIndexed { y: Int, l: BooleanArray ->
    l.asList().mapIndexedNotNull { x, b ->
        if (b) null else XY(x, y)
    }
}

fun dfs(x: Int, y: Int, steps: Int) {
    if (x !in 0 until grid[0].size || y !in grid.indices || grid[y][x]) {
        return
    }
    if (stepsToEnd[y][x] != Int.MAX_VALUE) {
        return
    }
    stepsToEnd[y][x] = steps
    dfs(x - 1, y, steps + 1)
    dfs(x + 1, y, steps + 1)
    dfs(x, y - 1, steps + 1)
    dfs(x, y + 1, steps + 1)
}
dfs(end.x, end.y, 0)

val minimumSteps = stepsToEnd[start.y][start.x]
println("Minimum steps is $minimumSteps")

if (debug) {
    for (l in stepsToEnd) {
        for (s in l) {
            print(if (s == Int.MAX_VALUE) ' ' else s % 10)
        }
        println()
    }
}

val stepsWithCheats = mutableMapOf<Pair<XY, XY>, Int>()
fun countCheats(start: XY) {
    val initialSteps = minimumSteps - stepsToEnd[start.y][start.x]
    data class Path(
        val xy: XY,
        val steps: Int,
    ) {
        val cheatsRemaining: Int
            get() = cheatsAvailable - steps

        val totalSteps: Int
            get() = initialSteps + steps + stepsToEnd[xy.y][xy.x]

        fun step(dx: Int, dy: Int): Path? {
            val x = xy.x + dx
            val y = xy.y + dy
            return if (x in grid[0].indices && y in grid.indices && cheatsRemaining > 0) {
                Path(XY(x, y), steps + 1)
            } else {
                null
            }
        }
    }

    val queue = LinkedList<Path>()
    val seen = mutableSetOf<XY>()
    queue.add(Path(start, 0))
    while (queue.isNotEmpty()) {
        val next = queue.removeFirst()
        if (!seen.add(next.xy)) continue

        if (next.xy != start && !grid[next.xy.y][next.xy.x]) {
            val savedSteps = minimumSteps - next.totalSteps
            if (savedSteps > 0) {
                stepsWithCheats[start to next.xy] = savedSteps
            }
        }
        next.step(-1, 0)?.let(queue::add)
        next.step(1, 0)?.let(queue::add)
        next.step(0, -1)?.let(queue::add)
        next.step(0, 1)?.let(queue::add)
    }
}

for (place in validPlaces) {
    countCheats(place)
}

if (debug) {
    stepsWithCheats.entries
        .groupBy { it.value }
        .mapValues { (_, v) -> v.size }
        .entries
        .sortedBy { it.key }
        .forEach { (k, v) ->
            if (v == 1) {
                println("There is one cheat that saves $k picoseconds.")
            } else {
                println("There are $v cheats that saves $k picoseconds.")
            }
        }
}
val cheatsThatSave100 = stepsWithCheats.entries
    .count { it.value >= 100 }

println("There are $cheatsThatSave100 cheats that save at least 100 picoseconds.")
println("Ran in ${startTime.elapsedNow()}")