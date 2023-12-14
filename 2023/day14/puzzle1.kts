#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun List<String>.countRocks(column: Int): Int {
    var value = 0
    var startRow = 0
    var rockCount = 0
    fun sumCurrentRocks() {
        val high = size - startRow
        val low = high - rockCount + 1
        value += (high + low) * rockCount / 2
        rockCount = 0
    }

    for (row in indices) {
        when (this[row][column]){
            'O' -> ++rockCount
            '.' -> continue
            '#' -> {
                sumCurrentRocks()
                startRow = row + 1
            }
            else -> error("Unknown char ${this[row][column]}")
        }
    }
    sumCurrentRocks()
    return value
}

val grid = generateSequence(::readlnOrNull).toList()
grid[0].indices
        .sumOf { grid.countRocks(it) }
        .let(::println)

println(start.elapsedNow())