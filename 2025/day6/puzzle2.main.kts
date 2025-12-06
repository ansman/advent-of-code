#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun List<String>.computeProblems(): List<List<Long>> {
    var i = 0
    val output = mutableListOf<List<Long>>()

    val length = get(0).length
    while (i < length) {
        val start = i
        while (i < length && any { it[i] != ' ' }) {
            ++i
        }
        val column = map { it.substring(start, i) }
        val width = column[0].length
        output += (0 until width).map { c ->
            column.joinToString("") { it[it.lastIndex - c].toString() }.trim().toLong()
        }
        ++i
    }
    return output.reversed()
}

val lines = generateSequence { readlnOrNull() }.toList()
val problems = lines.dropLast(1).computeProblems()
val operations = lines.last().trim().split(Regex("""\s+""")).reversed()

val sum = problems.withIndex().sumOf { (i, problem) ->
    val operation: (Long, Long) -> Long = when (val op = operations[i]) {
        "*" -> Long::times
        "+" -> Long::plus
        else -> error("Unknown operation '$op'")
    }
    problem.reduce(operation)
}
println(sum)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")