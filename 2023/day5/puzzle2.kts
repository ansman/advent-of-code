#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

fun parseMap(): TreeMap<LongRange, Long>? {
    val map = TreeMap<LongRange, Long>(Comparator.comparing { it.first })
    val type = readlnOrNull() ?: return null
    while (true) {
        val (destination, source, count) = readlnOrNull()
                ?.ifEmpty { null }
                ?.split(" ")
                ?.map { it.toLong() }
                ?: break
        map[source until (source + count)] = destination
    }
    return map
}

fun LongRange.intersect(other: LongRange): LongRange =
        max(first, other.first)..min(last, other.last)

fun TreeMap<LongRange, Long>.getLowerBound(source: LongRange): LongRange =
        floorKey(source) ?: entries.first().key

fun TreeMap<LongRange, Long>.getUpperBound(source: LongRange): LongRange =
        floorKey(source.last..source.last) ?: entries.last().key

fun TreeMap<LongRange, Long>.getDestination(source: LongRange): List<LongRange> {
    val lower = getLowerBound(source)
    val upper = getUpperBound(source)

    val entries = subMap(lower, true, upper, true).entries.toMutableList()
    val mapped = mutableListOf<LongRange>()
    var start = source.first
    var i = 0
    while (start <= source.last) {
        val next = entries.getOrNull(i)
        if (next == null) {
            mapped.add(start..source.last)
            break
        } else if (next.key.last < start) {
            // Range is completely before start
            ++i
            continue
        } else if (start < next.key.first) {
            // Range is completely after start
            mapped.add(start until next.key.first)
            start = next.key.first
        } else {
            check(start in next.key)
            val end = min(next.key.last, source.last)
            val destinationStart = next.value + (start - next.key.first)
            val destinationEnd = destinationStart + (end - start)
            mapped.add(destinationStart..destinationEnd)
            ++i
            start = end + 1
        }
    }
    return mapped
}

val seeds = readln()
        .removePrefix("seeds: ")
        .split(" ")
        .map { it.toLong() }
        .chunked(2)
        .map { (a, b) -> a until (a + b) }
        .toList()

readln()

val maps = generateSequence { parseMap() }.toList()

maps
        .fold(seeds) { ranges, map ->
            ranges.flatMap { range -> map.getDestination(range) }
        }
        .minOf { it.first }
        .let { println(it) }

println(start.elapsedNow())