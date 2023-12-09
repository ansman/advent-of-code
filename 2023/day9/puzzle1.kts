#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

tailrec fun List<Int>.predictNext(add: Int = 0): Int {
    val diff = zipWithNext().map { (a, b) -> b - a }
    return if (diff.all { it == 0 }) {
        add + last()
    } else {
        diff.predictNext(add + last())
    }
}

generateSequence(::readlnOrNull)
        .map { line -> line.split(" ").map { it.toInt() } }
        .map { it.predictNext() }
        .sum()
        .let(::println)

println(start.elapsedNow())