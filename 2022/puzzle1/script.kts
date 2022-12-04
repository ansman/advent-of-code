#!/usr/bin/env kotlin

import kotlin.math.max

var maxCalories = 0L
var isDone = false
while (!isDone) {
    var calories = 0L
    while (true) {
        val line = readLine()
        isDone = line == null
        if (line.isNullOrBlank()) break
        calories += line.toLong()
    }
    maxCalories = max(calories, maxCalories)
}

println(maxCalories)