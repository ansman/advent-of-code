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

val start = TimeSource.Monotonic.markNow()

val robots = generateSequence { readlnOrNull() }
    .map { parseRobot(it) }
    .toList()
repeat(100) {
    robots.forEach(Robot::move)
}

var tl = 0
var tr = 0
var br = 0
var bl = 0

if (debug) {
    val grid = Array<IntArray>(h) { IntArray(w) }
    for (robot in robots) {
        ++grid[robot.position.y][robot.position.x]
    }
    for (y in 0 until h) {
        for (x in 0 until w) {
            print(if (grid[y][x] > 0) grid[y][x] else '.')
        }
        println()
    }
}

for (robot in robots) {
    debug(robot)
    val left = robot.position.x < w / 2
    val top = robot.position.y < h / 2
    val right = robot.position.x > w / 2
    val bottom = robot.position.y > h / 2
    when {
        top && left -> tl++
        top && right -> tr++
        bottom && right -> br++
        bottom && left -> bl++
    }
}
debug("tl=$tl tr=$tr br=$br bl=$bl")

val safetyFactor = tl * tr * br * bl
println("Safety factor: $safetyFactor")
println("Ran in ${start.elapsedNow()}")