#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun calculateDistance(pressTime: Long, raceTime: Long): Long {
    val velocity = pressTime
    val remainingTime = raceTime - pressTime
    return remainingTime * velocity
}

fun calculateWinningRaces(time: Long, distance: Long): Long {
    var startOfWinningPressTime = Long.MAX_VALUE
    var pressTime = 0L
    while (pressTime <= time) {
        val currentDistance = calculateDistance(pressTime, time)
        if (currentDistance > distance) {
            startOfWinningPressTime = min(startOfWinningPressTime, pressTime)
        } else if (startOfWinningPressTime != Long.MAX_VALUE) {
            break
        }
        ++pressTime
    }
    return pressTime - startOfWinningPressTime
}

val time = readln().removePrefix("Time:").trim().replace(Regex("""\s+"""), "").toLong()
val distance = readln().removePrefix("Distance:").trim().replace(Regex("""\s+"""), "").toLong()
println(calculateWinningRaces(time, distance))
println(start.elapsedNow())