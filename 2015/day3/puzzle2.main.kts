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
    var mX = 0
    var mY = 0
    var rX = 0
    var rY = 0
    val visited = mutableSetOf<XY>()
    visited.add(XY(0, 0))
    input.forEachIndexed { i, c ->
        when (c) {
            '^' -> if (i % 2 == 0) ++mY else ++rY
            'v' -> if (i % 2 == 0) --mY else --rY
            '<' -> if (i % 2 == 0) --mX else --rX
            '>' -> if (i % 2 == 0) ++mX else ++rX
        }
        visited.add(XY(mX, mY))
        visited.add(XY(rX, rY))
    }
    println(visited.size)
}

println("Ran in ${startTime.elapsedNow()}")