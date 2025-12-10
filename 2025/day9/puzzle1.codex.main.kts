#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.time.TimeSource

// Reads coordinates, treats any two red tiles as opposite rectangle corners (inclusive edges),
// and finds the maximum possible rectangle area.
data class Coord(val x: Long, val y: Long)

val start = TimeSource.Monotonic.markNow()

val tiles = generateSequence { readlnOrNull() }
    .mapNotNull { line ->
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return@mapNotNull null
        val parts = trimmed.split(",")
        if (parts.size != 2) return@mapNotNull null
        val (x, y) = parts
        Coord(x.toLong(), y.toLong())
    }
    .toList()

var maxArea = if (tiles.isEmpty()) 0L else 1L // single tile counts as area 1
for (i in tiles.indices) {
    val a = tiles[i]
    for (j in i + 1 until tiles.size) {
        val b = tiles[j]
        val width = (a.x - b.x).absoluteValue + 1
        val height = (a.y - b.y).absoluteValue + 1
        val area = width * height
        if (area > maxArea) maxArea = area
    }
}

println(maxArea)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")
