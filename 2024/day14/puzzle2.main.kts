#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

val w = 101
val h = 103

data class Position(var x: Int, var y: Int) {
    companion object {
        fun parse(line: String): Position =
            line.removePrefix("p=").split(",").let { (x, y) ->
                Position(
                    x = x.toInt(),
                    y = y.toInt()
                )
            }
    }
}

data class Velocity(val x: Int, val y: Int) {
    companion object {
        fun parse(line: String): Velocity =
            line.removePrefix("v=").split(",").let { (x, y) ->
                Velocity(
                    x = x.toInt(),
                    y = y.toInt()
                )
            }
    }
}

data class Robot(
    val position: Position,
    val velocity: Velocity,
) {
    fun move() {
        position.x = (position.x + velocity.x + w) % w
        position.y = (position.y + velocity.y + h) % h
    }
}

fun parseRobot(line: String): Robot =
    Robot(
        position = Position.parse(line.substringBefore(' ')),
        velocity = Velocity.parse(line.substringAfter(' ')),
    )

fun IntArray.horizontallySymmetrical(): Boolean {
    val n = size
    for (i in 0 .. n / 2) {
        if (debug) {
            check(this[i] >= 0)
            check(this[n - i - 1] >= 0)
        }
        if ((this[i] > 0) && (this[n - i - 1] > 0)) {
            return true
        }
    }
    return false
}

fun Array<IntArray>.verticallySymmetrical(): Boolean {
    repeat(w) { x ->
        for (y in 0 until h / 2) {
            if ((this[y][x] > 0) && (this[h - y - 1][x] > 0)) {
                return true
            }
        }
    }
    return false
}

fun isChristmasTree(): Boolean {
    val baseWidth = 3
    val baseHeight = 3
    repeat(w - baseWidth) { x ->
        repeat(h - baseHeight) middle@ { h ->
            repeat(baseWidth) { dx ->
                repeat(baseHeight) { dy ->
                    if (grid[h + dy][x + dx] == 0) {
                        return@middle
                    }
                }
            }
            return true
        }
    }
    return false
}

fun printGrid() {
    if (!debug) return
    for (y in 0 until h) {
        for (x in 0 until w) {
            print(if (grid[y][x] > 0) grid[y][x] else '.')
        }
        println()
    }
}

val start = TimeSource.Monotonic.markNow()

val grid = Array(h) { IntArray(w) }
val robots = generateSequence { readlnOrNull() }
    .map { parseRobot(it) }
    .onEach { ++grid[it.position.y][it.position.x] }
    .toList()

var secondsPassed = 0
while (!isChristmasTree()) {
    for (robot in robots) {
        --grid[robot.position.y][robot.position.x]
        robot.move()
        ++grid[robot.position.y][robot.position.x]
    }
    ++secondsPassed
    if (secondsPassed % 1000000 == 0) {
        println("So far $secondsPassed seconds has passed")
    }
}

printGrid()

println("Seconds passed: $secondsPassed")
println("Ran in ${start.elapsedNow()}")