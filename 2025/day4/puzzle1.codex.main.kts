#!/usr/bin/env kotlin

// Counts rolls ('@') that have fewer than four adjacent rolls in the 8-neighborhood.

val grid = generateSequence { readlnOrNull() }
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .toList()

require(grid.isNotEmpty()) { "Input grid is empty" }
val width = grid.first().length
require(grid.all { it.length == width }) { "All rows must be the same length" }

val directions = listOf(
    -1 to -1, 0 to -1, 1 to -1,
    -1 to 0, /*skip self*/ 1 to 0,
    -1 to 1, 0 to 1, 1 to 1,
)

var accessible = 0

for (y in grid.indices) {
    for (x in 0 until width) {
        if (grid[y][x] != '@') continue

        var neighbors = 0
        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy
            if (nx !in 0 until width || ny !in grid.indices) continue
            if (grid[ny][nx] == '@') neighbors++
        }

        if (neighbors < 4) accessible++
    }
}

println(accessible)
