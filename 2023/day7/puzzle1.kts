#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

val bestCards = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
val bestHands = listOf(
        listOf(1, 1, 1, 1, 1),
        listOf(1, 1, 1, 2),
        listOf(1, 2, 2),
        listOf(1, 1, 3),
        listOf(2, 3),
        listOf(1, 4),
        listOf(5),
)

class Hand(hand: String) : Comparable<Hand> {
    private val value = bestHands.indexOf(hand.groupBy { it }.entries.map { it.value.size }.sorted())
    private val cards = hand.map { bestCards.indexOf(it) }.joinToString("") { it.toString(16) }

    override fun compareTo(other: Hand): Int = compareValuesBy(this, other, { it.value }, { it.cards })
}

generateSequence { readlnOrNull() }
        .map { it.split(" ") }
        .map { (h, b) -> Hand(h) to b.toInt() }
        .sortedBy { it.first }
        .mapIndexed { i, (hand, bid) -> (i + 1) * bid.toInt() }
        .sum()
        .let(::println)

println(start.elapsedNow())