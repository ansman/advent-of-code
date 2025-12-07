#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

val grid = generateSequence { readlnOrNull() }.mapTo(mutableListOf()) { it.toCharArray() }

operator fun List<CharArray>.contains(coordinate: Coordinate) =
    coordinate.y in grid.indices && coordinate.x in this[coordinate.y].indices

operator fun List<CharArray>.get(coordinate: Coordinate): Char = this[coordinate.y][coordinate.x]
operator fun List<CharArray>.set(coordinate: Coordinate, c: Char) {
    this[coordinate.y][coordinate.x] = c
}

data class Coordinate(val x: Int, val y: Int) {
    fun offset(dx: Int = 0, dy: Int = 0): Coordinate = Coordinate(x + dx, y + dy)
    override fun toString(): String = "$x,$y"
}

val start = grid.indices
    .asSequence()
    .flatMap { y ->
        grid[0].indices.asSequence()
            .map { Coordinate(it, y) }
            .filter { grid[it] == 'S' }
    }
    .first()

val beams = mutableSetOf(start)
val memo = mutableMapOf<Coordinate, Long>()
fun computeUniqueWays(c: Coordinate, log: Boolean = false): Long = memo.getOrPut(c) {
    val prev = c.offset(dy = -1)
    when {
        prev == start -> 1
        prev.y < 0 -> 0
        grid[prev] == '^' -> 0
        else -> {
            val prevLeft = prev.offset(dx = -1)
            val prevRight = prev.offset(dx = 1)
            val above = computeUniqueWays(prev)
            val left = if (prevLeft in grid && grid[prevLeft] == '^') {
                computeUniqueWays(prevLeft.offset(dy = -1))
            } else {
                0
            }
            val right = if (prevRight in grid && grid[prevRight] == '^') {
                computeUniqueWays(prevRight.offset(dy = -1))
            } else {
                0
            }
            above + left + right
        }
    }.also { if(log) println("$c -> $it") }
}

val worlds = grid[0].indices.sumOf { x ->
    computeUniqueWays(Coordinate(x, grid.lastIndex))
}

println(worlds)
println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")