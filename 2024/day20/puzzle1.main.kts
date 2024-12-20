#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

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
    .map {  l -> BooleanArray(l.length) { l[it] == '#' } }
    .toList()


data class Path(
    val xy: XY,
    val steps: Int,
    val cheatsRemaining: Int,
) {
    fun step(dx: Int, dy: Int): Path? {
        val x = xy.x + dx
        val y = xy.y + dy
        return when {
            x !in 0 until grid[0].size || y !in grid.indices -> null
            grid[xy.y][xy.x] -> if (cheatsRemaining == 0) {
                null
            } else {
                Path(XY(x, y), steps + 1, cheatsRemaining - 1)
            }
            !grid[y][x] -> Path(XY(x, y), steps + 1, cheatsRemaining)
            cheatsRemaining >= 2 -> Path(XY(x, y), steps + 1, cheatsRemaining - 1)
            else -> null
        }
    }
}

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

val stepsWithCheats = mutableMapOf<Int, Int>()
fun countCheats(x: Int, y: Int, cheatsRemaining: Int, steps: Int) {
    if (x !in 0 until grid[0].size || y !in grid.indices) {
        return
    }
    if (cheatsRemaining > 0) {
        countCheats(x - 1, y, cheatsRemaining - 1, steps + 1)
        countCheats(x + 1, y, cheatsRemaining - 1, steps + 1)
        countCheats(x, y - 1, cheatsRemaining - 1, steps + 1)
        countCheats(x, y + 1, cheatsRemaining - 1, steps + 1)
    } else if (!grid[y][x] && stepsToEnd[y][x] != Int.MAX_VALUE) {
        val savedSteps = minimumSteps - (steps + stepsToEnd[y][x])
        if (savedSteps > 0) {
            stepsWithCheats[savedSteps] = stepsWithCheats.getOrDefault(savedSteps, 0) + 1
        }
    }
}

for ((x, y) in validPlaces) {
    check(stepsToEnd[y][x] != Int.MAX_VALUE)
    countCheats(x, y, 2, minimumSteps - stepsToEnd[y][x])
}

if (debug) {
    stepsWithCheats.entries.sortedBy { it.key }.forEach { (k, v) ->
        if (v == 1) {
            println("There is one cheat that saves $k picoseconds.")
        } else {
            println("There are $v cheats that saves $k picoseconds.")
        }
    }
}
val cheatsThatSave100 = stepsWithCheats.entries
    .filter { it.key >= 100 }
    .sumOf { it.value }

println("There are $cheatsThatSave100 cheats that save at least 100 picoseconds.")
println("Ran in ${startTime.elapsedNow()}")