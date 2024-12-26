#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

generateSequence { readlnOrNull() }.forEach {
    val floor = it.count { it == '(' } - it.count { it == ')' }
    println(floor)
}

println("Ran in ${startTime.elapsedNow()}")