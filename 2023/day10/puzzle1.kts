#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun computePosition(x: Int, y: Int): Long = (x.toLong() shl 32) or y.toLong()

data class Step(val x: Int, val y: Int, val distance: Int = 0) {
    val pos get() = computePosition(x, y)
}

val grid = generateSequence { readlnOrNull() }
        .toList()

fun connectionsFor(x: Int, y: Int): Set<Pair<Int, Int>> = when (val t = grid[y][x]) {
    '.' -> emptySet()
    '|' -> setOf(x to y - 1, x to y + 1)
    '-' -> setOf(x - 1 to y, x + 1 to y)
    'L' -> setOf(x to y - 1, x + 1 to y)
    'J' -> setOf(x to y - 1, x - 1 to y)
    '7' -> setOf(x to y + 1, x - 1 to y)
    'F' -> setOf(x to y + 1, x + 1 to y)
    'S' -> setOf(x to y - 1, x to y + 1, x - 1 to y, x + 1 to y)
    else -> error("Unknown tile $t")
}

fun areConnected(x1: Int, y1: Int, x2: Int, y2: Int): Boolean =
        connectionsFor(x1, y1).contains(x2 to y2) &&
                connectionsFor(x2, y2).contains(x1 to y1)

fun printSeen() {
    if (!debug) return
    println()
    for (y in seen.indices) {
        for (x in seen[y].indices) {
            print(if (seen[y][x]) '#' else '.')
        }
        println()
    }
    println()
}

fun isSeen(x: Int, y: Int): Boolean = seen[y][x]

val startY = grid.indexOfFirst { 'S' in it }
val startX = grid[startY].indexOf('S')
val seen = List(grid.size) { BooleanArray(grid[it].length) }
var queue = ArrayDeque(listOf(Step(startX, startY)))
var maxSteps = 0
while (queue.isNotEmpty()) {
    val next = queue.removeFirst()
    if (isSeen(next.x, next.y)) continue
    printSeen()
    seen[next.y][next.x] = true
    if (debug) println("Visiting $next")
    maxSteps = max(maxSteps, next.distance)
    for (dy in -1..1) {
        for (dx in -1..1) {
            if ((dx == 0) == (dy == 0)) continue
            val x = next.x + dx
            val y = next.y + dy
            if (debug) println("  Checking $x, $y")
            if (x < 0 || y < 0 || y >= grid.size || x >= grid[y].length) continue
            if (debug) println("    Is not out of bounds")
            if (!areConnected(next.x, next.y, x, y)) continue
            if (debug) println("    Are connected")
            if (isSeen(x, y)) continue
            if (debug) println("    Is not seen")
            queue.addLast(Step(x, y, next.distance + 1))
        }
    }
}

println(maxSteps)

println(start.elapsedNow())