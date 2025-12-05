#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val freshRanges = generateSequence { readlnOrNull()?.ifBlank { null } }
    .map { it.split('-').map(String::toLong) }
    .map { (f, t) -> f..t }
    .toList()

val available = generateSequence { readlnOrNull() }
    .map { it.toLong() }
    .filter { number -> freshRanges.any { number in it } }
    .distinct()
    .count()

println(available)

println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")