#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

data class Coord(val x: Long, val y: Long) {
    fun areaWith(other: Coord): Long = (x - other.x + 1).absoluteValue * (y - other.y + 1).absoluteValue
}

val tiles = generateSequence { readlnOrNull() }
    .map { it.split(",") }
    .map { (x, y) -> Coord(x.toLong(), y.toLong()) }
    .toList()

val max = tiles.maxOf { tile ->
    tiles.maxOf { other -> tile.areaWith(other) }
}

println(max)


println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")