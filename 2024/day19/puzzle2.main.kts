#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

val patterns = readln().split(", ").groupBy { it.first() }

fun permutations(design: String): Long {
    val memo = LongArray(design.length) { -1 }
    fun checkPatternsAt(index: Int): Long {
        if (index >= design.length) return 1
        var value = memo[index]
        if (value == -1L) {
            value = patterns[design[index]]
                ?.sumOf { if (design.startsWith(it, index)) checkPatternsAt(index + it.length) else 0 }
                ?: 0
            memo[index] = value
        }
        return value
    }
    return checkPatternsAt(0)
}

readln()
val valid = generateSequence { readlnOrNull() }
    .sumOf { permutations(it) }
println("There are $valid valid designs")
println("Ran in ${startTime.elapsedNow()}")