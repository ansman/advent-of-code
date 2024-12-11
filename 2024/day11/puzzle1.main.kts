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

fun Long.split(): List<Long> {
    val digits = log10(this.toDouble()).toLong() + 1
    if (digits % 2L != 0L) {
        return listOf(this * 2024)
    }
    val split = 10.0.pow(digits / 2.0).toLong()
    val left = this / split
    val right = this % split
    return listOf(left, right)
}

val iterations = 25
var numbers = readln().split(' ').map { it.toLong() }
debug("Initial arrangement:")
debug(numbers.joinToString(" "))
repeat(iterations) {
    numbers = numbers.flatMap { number ->
        when {
            number == 0L -> listOf(1)
            else -> number.split()
        }
    }
    debug("")
    debug("After ${it + 1} ${if (it == 0) "blink" else "blinks"}")
    debug(numbers.joinToString(" "))
}

println("Number of stones is ${numbers.size}")
println("Ran in ${start.elapsedNow()}")