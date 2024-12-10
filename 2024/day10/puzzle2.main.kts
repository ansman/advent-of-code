#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = false
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()

val input = generateSequence { readlnOrNull() }
    .map { l -> l.map { it.digitToIntOrNull() ?: -1 } }
    .toList()
val w = input[0].size
val h = input.size

fun countScore(x: Int, y: Int, target: Int = 0): Int {
    if (x !in 0 until w || y !in 0 until h) {
        return 0
    }
    if (input[y][x] != target) {
        return 0
    }
    if (target == 9) {
        return 1
    }
    return countScore(x + 1, y, target + 1) +
            countScore(x - 1, y, target + 1) +
            countScore(x, y + 1, target + 1) +
            countScore(x, y - 1, target + 1)
}

var totalScore = 0
repeat(w) { x ->
    repeat(h) { y ->
        if (input[y][x] == 0) {
            totalScore += countScore(x, y)
        }
    }
}

println("Total score is $totalScore")
println("Ran in ${start.elapsedNow()}")