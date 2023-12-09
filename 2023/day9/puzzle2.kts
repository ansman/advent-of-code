#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun List<Int>.predictNext(): Int {
    val diff = reversed().zipWithNext().map { (a, b) -> a - b }.reversed()
    return if (diff.all { it == 0 }) {
        first()
    } else {
        first() - diff.predictNext()
    }
}

generateSequence(::readlnOrNull)
        .map { line -> line.split(" ").map { it.toInt() } }
        .map { it.predictNext() }
        .sum()
        .let(::println)

println(start.elapsedNow())