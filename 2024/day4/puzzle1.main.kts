#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val grid = generateSequence { readlnOrNull() }.toList()
val width = grid[0].length
val height = grid.size
val target = "XMAS"
fun matches(x: Int, y: Int, dx: Int, dy: Int, offset: Int = 0): Boolean =
    when {
        x !in 0 until width || y !in 0 until height -> false
        grid[y][x] != target[offset] -> false
        offset == target.lastIndex -> true
        else -> matches(x + dx, y + dy, dx, dy, offset + 1)
    }

var found = 0
grid.forEachIndexed { y, line ->
    line.forEachIndexed { x, _ ->
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) {
                    continue
                }
                if (matches(x, y, dx, dy)) {
                    ++found
                }
            }
        }
    }
}

println("Found $found instances of $target")
println("Ran in ${start.elapsedNow()}")