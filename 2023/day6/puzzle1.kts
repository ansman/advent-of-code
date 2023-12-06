#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun calculateDistance(pressTime: Int, raceTime: Int): Int {
    val velocity = pressTime
    val remainingTime = raceTime - pressTime
    return remainingTime * velocity
}

fun calculateWinningRaces(time: Int, distance: Int): Int {
    var startOfWinningPressTime = Int.MAX_VALUE
    var pressTime = 0
    while (pressTime <= time) {
        val currentDistance = calculateDistance(pressTime, time)
        if (currentDistance > distance) {
            startOfWinningPressTime = min(startOfWinningPressTime, pressTime)
        } else if (startOfWinningPressTime != Int.MAX_VALUE) {
            break
        }
        ++pressTime
    }
    return pressTime - startOfWinningPressTime
}

val races = readln().removePrefix("Time:").trim().split(Regex("""\s+""")).map { it.toInt() }
        .zip(readln().removePrefix("Distance:").trim().split(Regex("""\s+""")).map { it.toInt() })
        .map { (t, d) -> calculateWinningRaces(t, d) }
        .onEachIndexed { index, i -> println("Race $index has $i winning races") }
        .reduce { acc, i -> acc * i }
        .let { println(it) }

println(start.elapsedNow())