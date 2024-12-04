#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
val start = TimeSource.Monotonic.markNow()
val grid = generateSequence { readlnOrNull() }.toList()
val width = grid[0].length
val height = grid.size

fun get(x: Int, y: Int): Char = if (x in 0 until width && y in 0 until height) grid[y][x] else '.'

fun matches(x: Int, y: Int): Boolean {
    if (get(x, y) != 'A') {
        return false
    }
    val valid = "SM"
    val tl = get(x - 1, y - 1)
    val tr = get(x + 1, y - 1)
    val br = get(x + 1, y + 1)
    val bl = get(x - 1, y + 1)
    if (tl !in valid || tr !in valid || br !in valid || bl !in valid) {
        return false
    }
    return if (tl == tr && bl == br && tl != br || tl == bl && tr == br && tl != tr) {
        if (debug) println("$x, $y: tl: $tl tr: $tr br: $br bl: $bl")
        if (debug) println("Found match at $x, $y")
        true
    } else {
        false
    }
}

var found = 0
grid.forEachIndexed { y, line ->
    line.forEachIndexed { x, _ ->
        if (matches(x, y)) {
            ++found
        }
    }
}

println("Found $found instances")
println("Ran in ${start.elapsedNow()}")