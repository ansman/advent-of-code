#!/usr/bin/env kotlin

val pairs = generateSequence { readlnOrNull() }
        .map { line -> line.split("   ").map { it.toInt() } }
        .toList()

val right = pairs.groupBy { it[1] }

val similarityScore = pairs.sumOf { (l) -> l * (right[l]?.size ?: 0) }
println(similarityScore)