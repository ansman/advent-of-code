#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.sign

val inputPattern = Regex("""Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""")

data class Point(val x: Int, val y: Int) {
    override fun toString(): String = "x=$x, y=$y"
}

fun Point.manhattanDistanceTo(other: Point) = (x - other.x).absoluteValue + (y - other.y).absoluteValue
data class Sensor(val position: Point, val beacon: Point) {
    val beaconDistance = position.manhattanDistanceTo(beacon)
    val minX get() = position.x - beaconDistance
    val maxX get() = position.x + beaconDistance
    val minY get() = position.y - beaconDistance
    val maxY get() = position.y + beaconDistance

    fun canHaveBeaconAt(point: Point): Boolean = position.manhattanDistanceTo(point) > beaconDistance
}

val sensors = buildList {
    while (true) {
        val line = readLine() ?: break
        val (sx, sy, bx, by) = inputPattern.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        add(Sensor(Point(sx, sy), Point(bx, by)))
    }
}

val minX = sensors.minOf { it.minX }
val maxX = sensors.maxOf { it.maxX }
val minY = sensors.minOf { it.minY }
val maxY = sensors.maxOf { it.maxY }

fun printDebugGrid() {
    for (y in minY..maxY) {
        print("% 3d ".format(y))
        for (x in minX..maxX) {
            val p = Point(x, y)
            var c = ' '
            for (sensor in sensors) {
                if (p == sensor.position) {
                    c = 'S'
                    break
                } else if (p == sensor.beacon) {
                    c = 'B'
                    break
                } else if (sensors.any { !it.canHaveBeaconAt(p) }) {
                    c = '#'
                    break
                }
            }
            print(c)
        }
        println()
    }
}

//printDebugGrid()
val y = args[0].toInt()
val beacons = sensors.mapTo(mutableSetOf()) { it.beacon }
println("Number of positions: " + (minX..maxX)
        .asSequence()
        .map { Point(it, y) }
        .filter { it !in beacons }
        .count { p ->
            sensors.any { !it.canHaveBeaconAt(p) }
        })