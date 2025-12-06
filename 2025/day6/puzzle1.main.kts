#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

val split = Regex("""\s+""")

val problems = generateSequence { readlnOrNull() }
    .map { it.trim() }
    .map { it.split(split) }
    .toList()

val numbers=  problems.dropLast(1).map { ns -> ns.map { it.toLong() } }
val operations = problems.last()

val sum = numbers[0].indices.sumOf { i ->
    val operation: (Long, Long) -> Long = when (val op = operations[i]) {
        "*" -> Long::times
        "+" -> Long::plus
        else -> error("Unknown operation $op")
    }
    numbers.map { it[i] }
        .reduce(operation)
}
println(sum)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")