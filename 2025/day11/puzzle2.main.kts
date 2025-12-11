#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

val devices = generateSequence { readlnOrNull() }
    .map { it.split(": ") }
    .map { (device, outputs) ->
        device to outputs.split(" ").toSet()
    }
    .toMap()

fun countPaths(
    from: String,
    to: String = "out",
    requiredVisit: Set<String> = setOf("dac", "fft"),
    memo: MutableMap<Pair<String, Set<String>>, Long> = mutableMapOf()
): Long = memo.getOrPut(from to requiredVisit) {
    if (from == to) return@getOrPut if (requiredVisit.isEmpty()) 1 else 0
    val rv = requiredVisit - from
    devices[from]!!.sumOf { countPaths(it, to, rv, memo) }
}

println(countPaths("svr", "out"))

println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")