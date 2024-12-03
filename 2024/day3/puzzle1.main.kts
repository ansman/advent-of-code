#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
generateSequence { readlnOrNull() }
    .flatMap { line -> Regex("""mul\((\d+),(\d+)\)""").findAll(line) }
    .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    .sum()
    .let { println(it) }
println("Ran in ${start.elapsedNow()}")