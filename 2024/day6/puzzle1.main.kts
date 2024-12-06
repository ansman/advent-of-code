#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = true
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()
val map = generateSequence { readlnOrNull() }
    .map { it.toCharArray() }
    .toList()

val width = map[0].size
val height = map.size

var x = -1
var y = map.indexOfFirst { line ->
    x = line.indexOf('^')
    x != -1
}
var dir = '^'
map[y][x] = 'X'

fun rotateDirection() {
    dir = when (dir) {
        '^' -> '>'
        '>' -> 'v'
        'v' -> '<'
        '<' -> '^'
        else -> error("Invalid direction")
    }
}

fun step() {
    var dx = 0
    var dy = 0
    when (dir) {
        '^' -> dy = -1
        'v' -> dy = 1
        '<' -> dx = -1
        '>' -> dx = 1
        else -> error("Invalid direction")
    }
    val next = map.getOrNull(y + dy)?.getOrNull(x + dx)
    if (next == '#') {
        rotateDirection()
    } else {
        x += dx
        y += dy
        if (x in 0 until width && y in 0 until height) {
            map[y][x] = 'X'
        }
    }
}

while (x in 0 until width && y in 0 until height) {
    step()
}
var steps = map.sumOf { l -> l.count { it == 'X' } }

println("Steps is $steps")
println("Ran in ${start.elapsedNow()}")