#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

val height = 7
val keys = mutableListOf<List<Int>>()
val locks = mutableListOf<List<Int>>()

fun parseInput(input: List<String>): List<Int> = buildList {
    repeat(input.first().length) { i ->
        add(input.count { it[i] == '#' })
    }
}

fun fits(key: List<Int>, lock: List<Int>): Boolean {
    for (i in key.indices) {
        if (key[i] + lock[i] > height) {
            return false
        }
    }
    return true
}

while (true) {
    val schematic = generateSequence { readlnOrNull()?.ifEmpty { null } }
        .toList()
    if (schematic.isEmpty()) {
        break
    }
    if (schematic.first().first() == '#') {
        locks.add(parseInput(schematic))
    } else {
        keys.add(parseInput(schematic))
    }
}

var fits = 0
for (key in keys) {
    for (lock in locks) {
        if (fits(key, lock)) {
            ++fits
        }
    }
}

println("$fits combinations")

println("Ran in ${startTime.elapsedNow()}")