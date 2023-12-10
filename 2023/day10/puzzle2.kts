#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

val grid = generateSequence { readlnOrNull() }
        .toList()

data class Step(val x: Int, val y: Int, val halfX: Boolean = false, val halfY: Boolean = false) {
    val isInBounds: Boolean
        get() = x >= 0 && (x + (if (halfX) 1 else 0)) < grid[0].length &&
                y >= 0 && (y + (if (halfY) 1 else 0)) < grid.size

    val isHalfStep: Boolean get() = halfX || halfY

    fun moveHalfStepInDirection(dx: Int, dy: Int): Step? {
        return when {
            dx == -1 -> when {
//                halfY -> return null
                halfX -> copy(halfX = false)
                else -> copy(x = x - 1, halfX = true)
            }
            dx == 1 -> when {
//                halfY -> return null
                halfX -> copy(x = x + 1, halfX = false)
                else -> copy(halfX = true)
            }
            dy == -1 -> when {
//                halfX -> return null
                halfY -> copy(halfY = false)
                else -> copy(y = y - 1, halfY = true)
            }
            dy == 1 -> when {
//                halfX -> return null
                halfY -> copy(y = y + 1, halfY = false)
                else -> copy(halfY = true)
            }
            else -> error("Invalid direction ($dx, $dy)")
        }
    }
}

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

fun printMap() {
    println()
    for (y in map.indices) {
        for (x in map[y].indices) {
            print(map[y][x])
        }
        println()
    }
    println()
}

fun isSeen(x: Int, y: Int): Boolean = map[y][x] != ' '

val startY = grid.indexOfFirst { 'S' in it }
val startX = grid[startY].indexOf('S')
val seen = List(grid.size) { BooleanArray(grid[it].length) }
val map = List(grid.size) { CharArray(grid[it].length) { ' ' } }
val stepsInLoop = mutableSetOf(Step(startX, startY))
var queue = ArrayDeque(listOf(Step(startX, startY)))
map[startY][startY] = grid[startY][startX]
while (queue.isNotEmpty()) {
    val step = queue.removeFirst()
    for (dy in -1..1) {
        for (dx in -1..1) {
            if ((dx == 0) == (dy == 0)) continue
            val x = step.x + dx
            val y = step.y + dy
            if (x < 0 || y < 0 || y >= grid.size || x >= grid[y].length) continue
            if (!areConnected(step.x, step.y, x, y)) continue
            stepsInLoop.add(step.moveHalfStepInDirection(dx, dy)!!)
            val next = Step(x, y)
            stepsInLoop.add(next)
            if (!isSeen(x, y)) {
                queue.addLast(next)
                map[y][x] = grid[y][x]
            }
        }
    }
}

fun markOutside(step: Step) {
    if (step.isHalfStep) return
    map[step.y][step.x] = 'O'
}

val outside = ArrayDeque(
        grid[0].indices.asSequence().flatMap { sequenceOf(Step(it, 0), Step(it, 0, halfX = true))  }
                .plus(grid[0].indices.asSequence().flatMap { sequenceOf(Step(it, grid.lastIndex), Step(it, grid.lastIndex, halfX = true))  })
                .plus(grid.indices.asSequence().flatMap { sequenceOf(Step(0, it), Step(0, it, halfY = true)) })
                .plus(grid.indices.asSequence().flatMap { sequenceOf(Step(grid[0].lastIndex, it), Step(grid[0].lastIndex, it, halfY = true)) })
                .filter { it.isInBounds }
                .filter { it !in stepsInLoop }
                .toList()
)

//stepsInLoop.sortedBy { it.toString() }.filter { !it.isHalfStep }.forEach { println(it) }
//println()
//stepsInLoop.sortedBy { it.toString() }.filter { it.isHalfStep }.forEach { println(it) }
val visited = outside.toMutableSet()
while (outside.isNotEmpty()) {
    if (debug) printMap()
    val step = outside.removeLast()
    if (debug) println("Handling $step as outside")
    markOutside(step)
    for (dy in -1..1) {
        for (dx in -1..1) {
            if ((dx == 0) == (dy == 0)) continue
            val next = step.moveHalfStepInDirection(dx, dy) ?: continue
            if (debug) println("  Checking $next")
            if (next in visited) continue
            if (debug) println("    Is not visited")
            if (!next.isInBounds) continue
            if (debug) println("    Is in bounds")
            if (next in stepsInLoop) continue
            if (debug) println("    Is not in loop")
            visited.add(next)
            outside.addLast(next)
            if (debug) println("    Added")
        }
    }
}

printMap()

println(map.sumOf {
    it.sumOf { c -> (if (c == ' ') 1 else 0).toInt() }
})

println(start.elapsedNow())