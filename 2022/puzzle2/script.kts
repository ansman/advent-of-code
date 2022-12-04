#!/usr/bin/env kotlin

import kotlin.math.max

val allCalories = mutableListOf<Long>()
var isDone = false
while (!isDone) {
    var calories = 0L
    while (true) {
        val line = readLine()
        isDone = line == null
        if (line.isNullOrBlank()) break
        calories += line.toLong()
    }
    allCalories.add(calories)
}

allCalories.sortDescending()
println(allCalories.take(3).sum())