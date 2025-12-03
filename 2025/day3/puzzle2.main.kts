#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun List<Int>.maxJoltage(): Long {
    var remaining = 12
    var joltage = 0L
    var first = 0
    while (remaining > 0) {
        var largest = -1
        var largestIndex = -1
        for (i in first..size - remaining) {
            val v = get(i)
            if (v > largest) {
                largest = v
                largestIndex = i
            }
        }
        joltage = joltage * 10 + largest
        first = largestIndex + 1
        --remaining
    }
    return joltage
}

val total = generateSequence { readlnOrNull() }
    .map { it.map { c -> c.digitToInt() } }
    .map { it.maxJoltage() }
    .sum()
println(total)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")