#!/usr/bin/env kotlin

import kotlin.math.absoluteValue

val pairs = generateSequence { readlnOrNull() }
        .map { line -> line.split("   ").map { it.toInt() } }
        .toList()

val left = pairs.mapTo(mutableListOf()) { it[0] }.apply { sort() }
val right = pairs.mapTo(mutableListOf()) { it[1] }.apply { sort() }

val sum = left.asSequence().zip(right.asSequence())
        .sumOf { (l, r) -> (l - r).absoluteValue }
println(sum)