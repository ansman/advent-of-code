#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun List<Int>.maxJoltage(): Int {
    var max = 0
    var i = 0
    while (i < size - 1) {
        val a = get(i) * 10
        var j = i + 1
        while (j < size) {
            max = max(max, a + get(j))
            ++j
        }
        ++i
    }
    return max
}

val total = generateSequence { readlnOrNull() }
    .map { it.map { c -> c.digitToInt() } }
    .map { it.maxJoltage() }
    .sum()
println(total)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")