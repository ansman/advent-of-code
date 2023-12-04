#!/usr/bin/env kotlin

import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt

val gamePattern = Regex("""Card\s+\d+:\s+""")
val numberSplit = Regex("""\s+""")

fun String.parseCard(): Int {
    val (winning, played) = replace(gamePattern, "")
            .split(" | ")
            .map { it.trim() }
            .map { numbers -> numbers.split(numberSplit).map { it.toInt() }.toSet() }
    return winning.intersect(played).size
}

val allCards = generateSequence { readlnOrNull() }
        .map { it.parseCard() }
        .withIndex()
        .toList()

var playedCards = 0
var pendingCards = allCards.toMutableList()

while (pendingCards.isNotEmpty()) {
    val card = pendingCards.removeLast()
    ++playedCards
    if (card.value > 0) {
        val i = card.index + 1
        pendingCards += allCards.subList(i, i + card.value)
    }
}

println(playedCards)

