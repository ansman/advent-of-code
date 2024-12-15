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
var robotY = 0
val grid = generateSequence { readlnOrNull() }
    .takeWhile { it.isNotBlank() }
    .map {
        it.asSequence()
            .flatMap { c ->
                when (c) {
                    '#' -> sequenceOf('#', '#')
                    'O' -> sequenceOf('[', ']')
                    '.' -> sequenceOf('.', '.')
                    '@' -> sequenceOf('@', '.')
                    else -> error("Unknown char $c")
                }
            }
            .joinToString("")
            .toCharArray()
    }
    .onEachIndexed { y, l ->
        val x = l.indexOf('@')
        if (x >= 0) {
            robotX = x
            robotY = y
        }
    }
    .toList()

val instructions = generateSequence { readlnOrNull() }.flatMap { it.asSequence() }

fun computeGpsCoordinate(x: Int, y: Int): Int = 100 * y + x

fun canMove(x: Int, y: Int, dx: Int, dy: Int, checkSideways: Boolean = true): Boolean {
    if (x !in 0 until grid[y].size || y !in grid.indices) {
        return false
    }
    return when (val c = grid[y][x]) {
        '.' -> true
        '#' -> false
        '@' -> canMove(x + dx, y + dy, dx, dy)
        '[' -> canMove(x + dx, y + dy, dx, dy) && (!checkSideways || dy == 0 || canMove(x + 1, y, dx, dy, checkSideways = false))
        ']' -> canMove(x + dx, y + dy, dx, dy) && (!checkSideways || dy == 0 || canMove(x - 1, y, dx, dy, checkSideways = false))
        else -> error("Unknown char $c")
    }
}

fun move(x: Int, y: Int, dx: Int, dy: Int, moveSideways: Boolean = true) {
    when (val c = grid[y][x]) {
        '.' -> return
        '@' -> {
            move(x + dx, y + dy, dx, dy)
            grid[y + dy][x + dx] = '@'
            grid[y][x] = '.'
        }
        '[' -> {
            move(x + dx, y + dy, dx, dy)
            if (dy != 0 && moveSideways) {
                move(x + 1, y, dx, dy, moveSideways = false)
            }
            grid[y + dy][x + dx] = '['
            grid[y][x] = '.'
        }
        ']' -> {
            move(x + dx, y + dy, dx, dy)
            if (dy != 0 && moveSideways) {
                move(x - 1, y, dx, dy, moveSideways = false)
            }
            grid[y + dy][x + dx] = ']'
            grid[y][x] = '.'
        }
        else -> error("Unknown char $c")
    }
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
    if (canMove(robotX, robotY, dx, dy)) {
        move(robotX, robotY, dx, dy)
        robotX += dx
        robotY += dy
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
        if (c == '[') {
            gpsSum += computeGpsCoordinate(x, y)
        }
    }
}

println("GPS coordinate sum: $gpsSum")

println("Ran in ${start.elapsedNow()}")