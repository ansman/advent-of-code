#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun List<String>.computeVerticalReflections(): Sequence<Int> {
    fun isReflectingAfter(col: Int): Boolean {
        var before = col
        var after = col + 1
        while (before >= 0 && after < this[0].length) {
            if (!all { r -> r[before] == r[after] }) return false
            before--
            after++
        }
        return true
    }
    return this[0].indices
            .asSequence()
            .take(this[0].length - 1)
            .filter { isReflectingAfter(it) }
            .map { it + 1 }
}

fun List<String>.computeHorizontalReflections(): Sequence<Int> {
    fun isReflectingAfter(row: Int): Boolean {
        var before = row
        var after = row + 1
        while (before >= 0 && after < size) {
            if (this[before] != this[after]) return false
            before--
            after++
        }
        return true
    }
    return indices
            .asSequence()
            .take(size - 1)
            .filter { isReflectingAfter(it) }
            .map { (it + 1) * 100 }
}

fun List<String>.computeReflections(): Set<Int> =
        (computeVerticalReflections() + computeHorizontalReflections())
                .toSet()

fun List<String>.unSmudge(row: Int, col: Int): List<String> = List(size) { r ->
    if (r == row) {
        this[r].replaceRange(col, col + 1, if (this[r][col] == '.') "#" else ".")
    } else {
        this[r]
    }
}

fun List<String>.unSmudge(): Sequence<List<String>> = sequence {
    for (row in indices) {
        for (col in this@unSmudge[row].indices) {
            yield(unSmudge(row, col))
        }
    }
}

fun List<String>.computeReflectionWithFixes(): Int {
    val before = computeReflections()
    return unSmudge()
            .map { it.computeReflections() }
            .filter { it.isNotEmpty() }
            .first { it != before }
            .minus(before)
            .first()
}

generateSequence(::readlnOrNull)
        .joinToString("\n")
        .split("\n\n")
        .map { it.lines() }
        .sumOf { it.computeReflectionWithFixes() }
        .let(::println)

println(start.elapsedNow())