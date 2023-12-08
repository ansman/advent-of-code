#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.*
import kotlin.system.exitProcess
import kotlin.time.TimeSource

val debug = false
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

val cache = mutableMapOf<Pair<String, Int>, Pair<String, Int>>()

fun stepsUntilEnd(start: String, offset: Int = 0): Pair<String, Int> {
    cache[start to offset % instructions.length]?.let { return it }
    val addedSteps = mutableListOf(start)
    while (true) {
        require(addedSteps.size < nodes.size * 2) {
            "Too many steps: $addedSteps"
        }
        val curr = addedSteps.last()
        val o = (offset + addedSteps.lastIndex) % instructions.length

        val instruction = instructions[o]
        if (debug) {
            println("Checking $curr, $o, $instruction")
        }
        val (left, right) = nodes.getValue(curr)

        val next = when (instruction) {
            'L' -> left
            'R' -> right
            else -> error("Unknown instruction: $instruction")
        }
        if (next.endsWith('Z')) {
            addedSteps.forEachIndexed { i, s ->
                cache[s to (offset + i) % instructions.length] = next to (addedSteps.size - i)
            }
            break
        }
        val c = cache[next to (o + 1) % instructions.length]
        if (c == null) {
            addedSteps += next
        } else {
            addedSteps.forEachIndexed { i, s ->
                cache[s to (offset + i) % instructions.length] = c.first to (addedSteps.size - i + c.second)
            }
            break
        }
    }
    return cache.getValue(start to offset % instructions.length)
}

val ghosts = nodes.keys.filter { it.last() == 'A' }
        .map { stepsUntilEnd(it) }
        .map { (s, c) -> s to c.toLong() }
        .toTypedArray()
var low = ghosts.minOf { it.second }
var high = ghosts.maxOf { it.second }
var round = 0
while (low < high) {
    if (++round % 1_000_000 == 0) {
        println("Round $round, $low, $high, ${start.elapsedNow()}")
    }
    if (debug) {
        println("Status: ${ghosts.contentToString()}")
        println("Cache: $cache")
        println("High: $high, Low: $low")
    }
    val h = high
    low = high
    ghosts.forEachIndexed { i, (node, steps) ->
        var s = steps
        var n = node
        if (debug) println("Checking ghost $i: $n, $steps")
        while (s < h) {
            val (finalStep, addedSteps) = stepsUntilEnd(n, (ghosts[i].second % instructions.length).toInt())
            s += addedSteps
            n = finalStep
            ghosts[i] = (finalStep to s).also {
                if (debug) {
                    println("Ghost $i has now stepped to $it (added $addedSteps)")
                }
            }
        }
        low = min(low, ghosts[i].second)
        high = max(high, ghosts[i].second)
    }
}
println(low)

println(start.elapsedNow())