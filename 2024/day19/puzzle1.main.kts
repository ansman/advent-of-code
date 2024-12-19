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

fun isPossible(design: String): Boolean {
    val memo = arrayOfNulls<Boolean?>(design.length)
    fun checkPatternsAt(index: Int): Boolean {
        if (index >= design.length) return true
        memo[index]?.let { return it }
        val valid = patterns[design[index]]
            ?.any { design.startsWith(it, index) && checkPatternsAt(index + it.length) }
            ?: false
        memo[index] = valid
        return valid
    }
    return checkPatternsAt(0)
}

readln()
val valid = generateSequence { readlnOrNull() }
    .filter { isPossible(it) }
    .count()
println("There are $valid valid designs")
println("Ran in ${startTime.elapsedNow()}")