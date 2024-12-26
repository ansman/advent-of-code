#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

generateSequence { readlnOrNull() }
    .map { l -> l.split('x').map { it.toInt() } }
    .sumOf { (l, w, h) ->
        val (s1, s2) = listOf(l, w, h).sorted()

        s1 * 2 + s2 * 2  + l * w * h
    }
    .let { println(it) }

println("Ran in ${startTime.elapsedNow()}")