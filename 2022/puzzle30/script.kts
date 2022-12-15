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
    fun stepXUntilPotentialBeacon(x: Int, y: Int): Int {
        val radius = radiusOfEmptySpaceAt(y)
        if (radius < 0) return x
        val minX = position.x - radius
        val maxX = position.x + radius
        return if (x in minX..maxX) {
            maxX + 1
        } else {
            x
        }
    }

    fun radiusOfEmptySpaceAt(y: Int): Int = (beaconDistance - (y - position.y).absoluteValue)
}

val sensors = buildList {
    while (true) {
        val line = readLine() ?: break
        val (sx, sy, bx, by) = inputPattern.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        add(Sensor(Point(sx, sy), Point(bx, by)))
    }
}

val s = Sensor(Point(1, 1), Point(1, 1))

val max = args[0].toInt()
val minX = 0
val maxX = max
val minY = 0
val maxY = max

val beacons = sensors.mapTo(mutableSetOf()) { it.beacon }
var y = minY
outer@while (y <= maxY) {
    var x = minX
    while (x <= maxX) {
        val xAtStart = x
        for (sensor in sensors) {
            x = sensor.stepXUntilPotentialBeacon(x, y)
        }
        if (x == xAtStart) {
            require(sensors.all { it.canHaveBeaconAt(Point(x, y)) })

            // X did not jump, it's empty
            println("Found empty space at $x, $y")
            println("Frequency: ${x * 4000000L + y}")
            break@outer
        }
    }
    ++y
}