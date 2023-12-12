#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun repair(
        springs: String,
        groups: List<Int>,
        springStart: Int = 0,
        groupIndex: Int = 0,
        cache: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
): Int = cache.getOrPut(springStart to groupIndex) {
    if (debug) println("Checking $springStart (${springs.getOrNull(springStart)}) $groupIndex")
    if (springStart >= springs.length) {
        if (debug) println("  End of springs")
        return@getOrPut if (groupIndex >= groups.size) {
            if (debug) println("    End of groups too, returning 1")
            1
        } else {
            if (debug) println("    Has more groups, returning 0")
            0
        }
    }

    fun runNext() = repair(springs, groups, springStart + 1, groupIndex, cache)

    if (springs[springStart] == '.') {
        if (debug) println("  Next is a whole spring, skipping")
        return@getOrPut runNext()
    }

    val mustBeGroup = springs[springStart] == '#'

    if (debug) println("  Must be group: $mustBeGroup")

    fun runNextGroup() = if (mustBeGroup) 0 else runNext()

    // We're out of groups
    if (groupIndex >= groups.size) {
        if (debug) println("  No more groups to consume")
        return@getOrPut runNextGroup()
    }


    // Now we try and mark this as a group
    val groupSize = groups[groupIndex]
    if (springStart + groupSize > springs.length) {
        if (debug) println("  Group will be out of bounds, can't be group")
        return@getOrPut runNextGroup()
    }

    for (i in springStart until springStart + groupSize) {
        if (springs[i] == '.') {
            if (debug) println("  Whole spring found before end of group, can't be group")
            return@getOrPut runNextGroup()
        }
    }

    // If the next is a spring this can't be a group because groups are continuous
    if (springs.getOrNull(springStart + groupSize) == '#') {
        if (debug) println("  Next is whole, can't be group")
        return runNextGroup()
    }

    // We consume one more than the group size because the next character cannot be a group start
    return runNextGroup() + repair(springs, groups, springStart + groupSize + 1, groupIndex + 1, cache)
}

generateSequence(::readlnOrNull)
        .map { it.split(" ") }
        .map { (springs, groups) -> repair(springs, groups.split(",").map { it.toInt() }) }
        .onEachIndexed { i, r -> if (debug) println("Row $i has $r repair ways") }
        .sum()
        .let(::println)

println(start.elapsedNow())