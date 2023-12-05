#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt
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

fun TreeMap<LongRange, Long>.getDestination(source: Long): Long {
    val (sourceRange, destinationStart) = floorEntry(source..source) ?: return source
    if (source !in sourceRange) return source
    return destinationStart + source - sourceRange.first
}

val seeds = readln()
        .removePrefix("seeds: ")
        .splitToSequence(" ")
        .map { it.toLong() }
        .toList()

readln()

val maps = generateSequence { parseMap() }.toList()

seeds
        .asSequence()
        .map { seed -> maps.fold(seed) { n, map -> map.getDestination(n) } }
        .min()
        .let { println(it) }

println(start.elapsedNow())