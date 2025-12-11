#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

val devices = generateSequence { readlnOrNull() }
    .map { it.split(": ") }
    .map { (device, outputs) ->
        device to outputs.split(" ").toSet()
    }
    .toMap()

fun countPaths(from: String, to: String = "out", memo: MutableMap<String, Int> = mutableMapOf()): Int =
    memo.getOrPut(from) {
        if (from == to) return@getOrPut 1
        devices[from]?.sumOf { countPaths(it, to, memo) } ?: 0
    }

println(countPaths("you", "out"))

println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")