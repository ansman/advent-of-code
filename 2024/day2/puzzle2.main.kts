#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
fun List<Int>.generateSkips(): Sequence<List<Int>> =
    indices.asSequence().map {
        toMutableList().apply { removeAt(it) }
    }

fun List<Int>.isSafe(): Boolean {
    val isIncreasing = (this[1] - this[0]).sign
    return this
        .zipWithNext()
        .all { (a, b) ->
            (b - a).absoluteValue in 1..3 && (b - a).sign == isIncreasing
        }
}

generateSequence { readlnOrNull() }
    .map { line -> line.split(" ").map { it.toInt() } }
    .filter { report ->
        report.isSafe() || report.generateSkips().any { it.isSafe() }
    }
    .count()
    .let { println(it) }
println("Ran in ${start.elapsedNow()}")