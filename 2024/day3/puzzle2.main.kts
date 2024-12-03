#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

var enabled = true
generateSequence { readlnOrNull() }
    .flatMap { line -> Regex("""mul\((\d+),(\d+)\)|do\(\)|don't\(\)""").findAll(line) }
    .filter {
        when (it.value) {
            "do()" -> {
                enabled = true
                false
            }
            "don't()" -> {
                enabled = false
                false
            }
            else -> enabled
        }
    }
    .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    .sum()
    .let { println(it) }
println("Ran in ${start.elapsedNow()}")