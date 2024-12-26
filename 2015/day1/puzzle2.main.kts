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
    var floor = 0
    it.forEachIndexed { i, c ->
        when (c) {
            '(' -> ++floor
            ')' -> --floor
            else -> error("Invalid character: $c")
        }
        if (floor == -1) {
            println(i + 1)
            return@forEach
        }
    }
}

println("Ran in ${startTime.elapsedNow()}")