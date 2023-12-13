#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun List<String>.computeVerticalReflection(): Int? {
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
            .take(this[0].length - 1)
            .firstOrNull { isReflectingAfter(it) }
}

fun List<String>.computeHorizontalReflection(): Int? {
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
    return this.indices
            .take(size - 1)
            .firstOrNull { isReflectingAfter(it) }
}

fun List<String>.computeReflection(): Int =
        computeVerticalReflection()
                ?.plus(1)
                ?: computeHorizontalReflection()?.plus(1)?.times(100)
                ?: error("No reflection found")

generateSequence(::readlnOrNull)
        .joinToString("\n")
        .split("\n\n")
        .map { it.lines() }
        .sumOf { it.computeReflection() }
        .let(::println)

println(start.elapsedNow())