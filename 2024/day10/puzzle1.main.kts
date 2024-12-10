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

fun countScore(x: Int, y: Int, target: Int = 0, reachedPoints: MutableSet<Pair<Int, Int>> = mutableSetOf()): Set<Pair<Int, Int>> {
    if (x !in 0 until w || y !in 0 until h) {
        return reachedPoints
    }
    if (input[y][x] != target) {
        return reachedPoints
    }
    if (target == 9) {
        reachedPoints.add(x to y)
        return reachedPoints
    }
    countScore(x + 1, y, target + 1, reachedPoints)
    countScore(x -1, y, target + 1, reachedPoints)
    countScore(x, y + 1, target + 1, reachedPoints)
    countScore(x, y - 1, target + 1, reachedPoints)
    return reachedPoints
}

var totalScore = 0
repeat(w) { x ->
    repeat(h) { y ->
        if (input[y][x] == 0) {
            totalScore += countScore(x, y).size
        }
    }
}

println("Total score is $totalScore")
println("Ran in ${start.elapsedNow()}")