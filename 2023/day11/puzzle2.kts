#!/usr/bin/env kotlin

import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

val expansionFactor = 1000000L

val sky = generateSequence(::readlnOrNull)
        .toList()

val expandColumns = BooleanArray(sky[0].length) { c -> sky.all { it[c] == '.' } }
val expandRows = BooleanArray(sky.size) { sky[it].all { c -> c == '.' } }

val galaxies = sky.indices.flatMap { r ->
    sky[r].indices.mapNotNull { c ->
        if (sky[r][c] == '#') r to c else null
    }
}

fun rowsBetween(from: Int, to: Int): Long {
    if (from > to) return rowsBetween(to, from)
    return (from until to).sumOf { if (expandRows[it]) expansionFactor else 1L }
}

fun columnsBetween(from: Int, to: Int): Long {
    if (from > to) return columnsBetween(to, from)
    return (from until to).sumOf { if (expandColumns[it]) expansionFactor else 1L }
}

fun shortestPath(from: Pair<Int, Int>, to: Pair<Int, Int>): Long =
        rowsBetween(from.first, to.first) + columnsBetween(from.second, to.second)

println(galaxies.indices.asSequence()
        .flatMap { i -> galaxies.indices.asSequence().drop(i + 1).map { galaxies[i] to galaxies[it] } }
        .map { (from, to) -> shortestPath(from, to) }
        .sum())

println(start.elapsedNow())