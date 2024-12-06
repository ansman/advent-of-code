#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = true
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

data class Position(val x: Int, val y: Int, val dir: Char)

val start = TimeSource.Monotonic.markNow()
val map = generateSequence { readlnOrNull() }.toList()

fun createMutableMap() = map.map { it.toCharArray() }

val width = map[0].length
val height = map.size

var startX = -1
var startY = map.indexOfFirst { line ->
    startX = line.indexOf('^')
    startX != -1
}

fun Char.rotateDirection(): Char =
    when (this) {
        '^' -> '>'
        '>' -> 'v'
        'v' -> '<'
        '<' -> '^'
        else -> error("Invalid direction")
    }

fun createsLoop(ox: Int, oy: Int): Boolean {
    var dir = '^'
    val map = createMutableMap()
    if (map[oy][ox] != '.') {
        return false
    }
    map[oy][ox] = '#'
    var x = startX
    var y = startY
    val seen = mutableSetOf(Position(x, y, dir))

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
            dir = dir.rotateDirection()
        } else {
            x += dx
            y += dy
        }
    }

    while (x in 0 until width && y in 0 until height) {
        step()
        if (!seen.add(Position(x, y, dir))) {
            return true
        }
    }
    return false
}

var loops = 0
repeat(height) { y ->
    repeat(width) { x ->
        if (createsLoop(x, y)) {
            ++loops
        }
    }
}

println("Possible loops is $loops")
println("Ran in ${start.elapsedNow()}")