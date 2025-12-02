#!/usr/bin/env kotlin

val pattern = Regex("""(\d+)\1""")

fun String.isRepeated(): Boolean = matches(pattern)

val number = generateSequence { readlnOrNull() }
    .flatMap { it.split(',') }
    .map { r -> r.split('-').map { it.toLong() } }
    .map { (f, t) -> f..t }
    .flatMap { it }
    .map { it.toString() }
    .filter { it.isRepeated() }
    .sumOf { it.toLong() }

println(number)