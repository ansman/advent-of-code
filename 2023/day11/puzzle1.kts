#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun List<String>.print() = apply {
    if (!debug) return@apply
    println(joinToString("\n", prefix = "\n", postfix = "\n"))
}

fun List<String>.expand(): List<String> {
    fun shouldExpandColumn(col: Int): Boolean = all { it[col] == '.' }
    fun shouldExpandRow(row: Int): Boolean = this[row].all { it == '.' }
    val expandColumns = BooleanArray(this[0].length, ::shouldExpandColumn)
    val expandRows = BooleanArray(size, ::shouldExpandRow)

    return flatMapIndexed { r, row ->
        val expanded = row.flatMapIndexed { c, cell ->
            if (expandColumns[c]) listOf(cell, cell) else listOf(cell)
        }.joinToString("")
        if (expandRows[r]) listOf(expanded, expanded) else listOf(expanded)
    }

}

val sky = generateSequence(::readlnOrNull)
        .toList()
        .print()
        .expand()
        .print()

val galaxies = sky.indices.flatMap { r ->
    sky[r].indices.mapNotNull { c ->
        if (sky[r][c] == '#') r to c else null
    }
}

fun shortestPath(from: Pair<Int, Int>, to: Pair<Int, Int>): Int =
        abs(from.first - to.first) + abs(from.second - to.second)

println(galaxies.indices.asSequence()
        .flatMap { i -> galaxies.indices.asSequence().drop(i + 1).map { galaxies[i] to galaxies[it] } }
        .map { (from, to) -> shortestPath(from, to) }
        .sum())

println(start.elapsedNow())