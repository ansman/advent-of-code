#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

val grid = generateSequence { readlnOrNull() }.mapTo(mutableListOf()) { it.toCharArray() }

operator fun List<CharArray>.get(coordinate: Coordinate): Char = this[coordinate.y][coordinate.x]
operator fun List<CharArray>.set(coordinate: Coordinate, c: Char) {
    this[coordinate.y][coordinate.x] = c
}

data class Coordinate(val x: Int, val y: Int) {
    fun offset(dx: Int = 0, dy: Int = 0): Coordinate = Coordinate(x + dx, y + dy)
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
var splits = 0
while (beams.isNotEmpty()) {
    val beam = beams.iterator().run { next().also { remove() } }
    val next = beam.offset(dy = 1)
    if (next.y >= grid.size) {
        continue
    }
    if (grid[next] == '^') {
        ++splits
        beams.add(next.offset(dx = -1).also { grid[it] = '|' })
        beams.add(next.offset(dx = 1).also { grid[it] = '|' })
    } else {
        beams.add(next.also { grid[it] = '|' })
    }
}

println(splits)
println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")