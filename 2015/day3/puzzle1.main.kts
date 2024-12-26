#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

data class XY(val x: Int, val y: Int)

generateSequence { readlnOrNull() }.forEach { input ->
    var x = 0
    var y = 0
    val visited = mutableSetOf<XY>()
    visited.add(XY(x, y))
    for (c in input) {
        when (c) {
            '^' -> ++y
            'v' -> --y
            '<' -> --x
            '>' -> ++x
        }
        visited.add(XY(x, y))
    }
    println(visited.size)
}

println("Ran in ${startTime.elapsedNow()}")