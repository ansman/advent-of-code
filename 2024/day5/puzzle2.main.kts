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
    .groupBy({ it[0] }, { it[1] })
    .mapValues { (_, v) -> v.toSet() }

fun List<Int>.isValid(): Boolean {
    forEachIndexed { index, i ->
        val rule = ordering[i] ?: return@forEachIndexed
        val before = take(index).toSet()
        if (before.intersect(rule).isNotEmpty()) {
            return false
        }
    }
    return true
}

fun List<Int>.order(): List<Int> = toMutableList().apply {
    while (true) {
        var didReorder = false
        for (i in 0 until lastIndex) {
            val a = get(i)
            val b = get(i + 1)
            val rb = ordering[b] ?: continue
            if (a in rb) {
                val tmp = get(i)
                set(i, get(i + 1))
                set(i + 1, tmp)
                didReorder = true
            }
        }
        if (!didReorder) {
            break
        }
    }
}

val sum = generateSequence { readlnOrNull() }
    .map { l -> l.split(',').map { it.toInt() } }
    .onEach { debug("Processing print $it") }
    .filterNot { it.isValid() }
    .map { order ->
        order.order().also {
            debug("Reordered $order to $it")
        }
    }
    .map { it[it.lastIndex / 2] }
    .sum()

println("Sum is $sum")
println("Ran in ${start.elapsedNow()}")