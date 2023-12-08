#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun String.parseNode(): Pair<String, Pair<String, String>> {
    val start = substringBefore(" =")
    val (left, right) = substringAfter(" = ")
            .removeSurrounding("(", ")")
            .split(", ")
    return start to (left to right)
}

val instructions = readln()
readln()
val nodes = generateSequence(::readlnOrNull)
        .map { it.parseNode() }
        .toMap()

var current = "AAA"
var instructionIndex = 0
while (current != "ZZZ") {
    val instruction = instructions[(instructionIndex)++ % instructions.length]
    val (left, right) = nodes.getValue(current)
    current = when (instruction) {
        'L' -> left
        'R' -> right
        else -> error("Unknown instruction: $instruction")
    }
}
println(instructionIndex)

println(start.elapsedNow())