#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}
val start = TimeSource.Monotonic.markNow()

var robotX = 0
var roboty = 0
val grid = generateSequence { readlnOrNull() }
    .onEachIndexed { y, l ->
        val x = l.indexOf('@')
        if (x >= 0) {
            robotX = x
            roboty = y
        }
    }
    .takeWhile { it.isNotBlank() }
    .map { it.toCharArray() }
    .toList()

val instructions = generateSequence { readlnOrNull() }.flatMap { it.asSequence() }

fun computeGpsCoordinate(x: Int, y: Int): Int = 100 * y + x

fun tryMove(x: Int, y: Int, dx: Int, dy: Int): Boolean {
    if (x !in 0 until grid[y].size || y !in grid.indices) {
        return false
    }
    debug("Trying to move $x,$y by $dx,$dy")
    return when (val c = grid[y][x]) {
        '.' -> true
        '#' -> false
        '@', 'O' -> if (tryMove(x + dx, y + dy, dx, dy)) {
            grid[y + dy][x + dx] = c
            grid[y][x] = '.'
            true
        } else {
            false
        }
        else -> error("Unknown char $c")
    }.also { debug("Could move: $it") }
}

fun execute(instruction: Char) {
    var dx = 0
    var dy = 0
    when (instruction) {
        '^' -> dy = -1
        'v' -> dy = 1
        '<' -> dx = -1
        '>' -> dx = 1
        else -> error("Unknown instruction $instruction")
    }
    if (tryMove(robotX, roboty, dx, dy)) {
        robotX += dx
        roboty += dy
    }
}

fun printGrid() {
    grid.forEach { println(it.concatToString()) }
    println()
}

if (debug) {
    println("Initial state:")
    printGrid()
}

instructions.forEach {
    execute(it)
    if (debug) {
        println("Move $it:")
        printGrid()
    }
}

var gpsSum = 0
grid.forEachIndexed { y, line ->
    line.forEachIndexed { x, c ->
        if (c == 'O') {
            gpsSum += computeGpsCoordinate(x, y)
        }
    }
}

println("GPS coordinate sum: $gpsSum")

println("Ran in ${start.elapsedNow()}")