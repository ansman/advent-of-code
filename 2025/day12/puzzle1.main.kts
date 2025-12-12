#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

typealias Shape = Array<BooleanArray>

class Board(val width: Int, val height: Int, val counts: IntArray)

val input = generateSequence { readlnOrNull() }.toList()
var i = 0
val shapes = mutableListOf<Shape>()
val boards = mutableListOf<Board>()
while (i < input.size) {
    while (input[i].endsWith(':')) {
        shapes += generateSequence { input[++i].ifBlank { null } }
            .map { l -> l.map { it == '#' }.toBooleanArray() }
            .toList()
            .toTypedArray<BooleanArray>()
        ++i
    }
    val (size, counts) = input[i++].split(": ")
    val (w, h) = size.split('x').map { it.toInt() }
    boards += Board(width = w, height = h, counts = counts.split(' ').map { it.toInt() }.toIntArray())
}
val areas = shapes.map { shape -> shape.sumOf { r -> r.count { it } } }

fun Board.fitsShapes(): Boolean {
    val area = width * height
    val shapeArea = counts.withIndex().sumOf { (i, c) -> c * areas[i] }
    return shapeArea <= area
}

boards
    .count { it.fitsShapes() }
    .let { println(it) }

println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")