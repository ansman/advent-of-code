#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()
val ordering = generateSequence { readlnOrNull() }
    .takeWhile { it.isNotBlank() }
    .map { l -> l.split('|').map { it.toInt() } }
    .groupBy({it[0]}, {it[1]})
    .mapValues { (_, v) -> v.toSet() }

fun List<Int>.isValid(): Boolean {
    forEachIndexed { index, i ->
        val rule = ordering[i] ?: return@forEachIndexed
        val before = take(index).toSet()
        debug("Checking $i with $rule and $before")
        if (before.intersect(rule).isNotEmpty()) {
            return false
        }
    }
    return true
}

val sum = generateSequence { readlnOrNull() }
    .map { l -> l.split(',').map { it.toInt() } }
    .onEach { debug("Processing print $it") }
    .filter { it.isValid() }
    .map { it[it.lastIndex / 2] }
    .sum()

println("Sum is $sum")
println("Ran in ${start.elapsedNow()}")