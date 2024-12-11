#!/usr/bin/env kotlin

import kotlin.math.log10
import kotlin.math.pow
import kotlin.time.TimeSource

val debug = false
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()


val iterations = 75

val seen: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()
fun iterate(
    number: Long,
    iterationsRemaining: Int = iterations,
): Long = seen.getOrPut(number to iterationsRemaining) {
    if (iterationsRemaining == 0) {
        // We only have one number left
        return@getOrPut 1
    }
    if (number == 0L) {
        return@getOrPut iterate(1L, iterationsRemaining - 1)
    }
    val digits = log10(number.toDouble()).toLong() + 1
    if (digits % 2L != 0L) {
        return@getOrPut iterate(number * 2024, iterationsRemaining - 1)
    }
    val split = 10.0.pow(digits / 2.0).toLong()
    val left = number / split
    val right = number % split
    iterate(left, iterationsRemaining - 1) + iterate(right, iterationsRemaining - 1)
}

var numbers = readln().split(' ').map { it.toLong() }
val numberOfStones = numbers.sumOf {
    iterate(it)
}

println("Number of stones is $numberOfStones")
println("Ran in ${start.elapsedNow()}")
