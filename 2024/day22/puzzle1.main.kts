#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

fun runIterations(number: Long, iterations: Int): Long {
    var num = number
    repeat(iterations) {
        num = (num xor (num shl 6)) % 16777216
        num = (num xor (num ushr 5)) % 16777216
        num = (num xor (num shl 11)) % 16777216
    }
    return num
}

generateSequence { readlnOrNull() }
    .map { runIterations(it.toLong(), 2000) }
    .sum()
    .let { println(it) }

println("Ran in ${startTime.elapsedNow()}")