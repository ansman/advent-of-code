#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.math.min
import kotlin.time.TimeSource

fun LongRange.overlaps(range: LongRange): Boolean =
    first in range || range.first in this || max(last, range.last) + 1 == min(first, range.first)

fun LongRange.mergeWith(range: LongRange): LongRange = min(first, range.first)..max(last, range.last)

class RangeSet {
    private val ranges = mutableListOf<LongRange>()

    fun add(range: LongRange) {
        var rangeToInsert = range
        val it = ranges.iterator()
        while (it.hasNext()) {
            val r = it.next()
            if (rangeToInsert.overlaps(r)) {
                rangeToInsert = r.mergeWith(rangeToInsert)
                it.remove()
            }
        }
        ranges.add(rangeToInsert)
    }

    fun countEntries(): Long = ranges.sumOf { (it.last - it.first) + 1 }
}

val start = TimeSource.Monotonic.markNow()
val rangeSet = RangeSet()
generateSequence { readlnOrNull()?.ifBlank { null } }
    .map { it.split('-').map(String::toLong) }
    .map { (f, t) -> f..t }
    .forEach { rangeSet.add(it) }

println(rangeSet.countEntries())
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")