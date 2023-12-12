#!/usr/bin/env kotlin

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun <T> Collection<T>.repeat(times: Int): List<T> {
    val list = ArrayList<T>(size * times)
    repeat(times) {
        list.addAll(this)
    }
    return list
}

fun String.unfold(times: Int): String = listOf(this).repeat(times).joinToString("?")

fun repair(springs: String, groups: List<Int>) = repair(springs, groups, 0, 0, mutableMapOf())

fun repair(
        springs: String,
        groups: List<Int>,
        springStart: Int,
        groupIndex: Int,
        cache: MutableMap<Pair<Int, Int>, Long>,
): Long = cache.getOrPut(springStart to groupIndex) {
    if (debug) println("Checking $springStart (${springs.getOrNull(springStart)}) $groupIndex (cache size ${cache.size})")
    if (springStart >= springs.length) {
        if (debug) println("  End of springs")
        return@getOrPut if (groupIndex >= groups.size) {
            if (debug) println("    End of groups too, returning 1")
            1L
        } else {
            if (debug) println("    Has more groups, returning 0")
            0L
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
        return@getOrPut runNextGroup()
    }

    // We consume one more than the group size because the next character cannot be a group start
    runNextGroup() + repair(springs, groups, springStart + groupSize + 1, groupIndex + 1, cache)
}

generateSequence(::readlnOrNull)
        .map { it.split(" ") }
        .map { (springs, groups) ->
            springs.unfold(5) to groups.split(",").map { it.toInt() }.repeat(5)
        }
        .onEach { (springs, _) -> if (debug) println("Processing $springs") }
        .map { (springs, groups) -> repair(springs, groups) }
        .onEachIndexed { i, r -> if (debug) println("Row $i has $r repair ways") }
        .sum()
        .let(::println)

println(start.elapsedNow())