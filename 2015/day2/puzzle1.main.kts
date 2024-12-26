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
        2*l*w + 2*w*h + 2*h*l + minOf(l*w, w*h, h*l)
    }
    .let { println(it) }

println("Ran in ${startTime.elapsedNow()}")