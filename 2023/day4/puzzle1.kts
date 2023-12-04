#!/usr/bin/env kotlin

import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()

val gamePattern = Regex("""Card\s+\d+:\s+""")
val numberSplit = Regex("""\s+""")

fun String.parseCard(): Int {
    val (winning, played) = replace(gamePattern, "")
            .split(" | ")
            .map { it.trim() }
            .map { numbers -> numbers.split(numberSplit).map { it.toInt() }.toSet() }
    val winningNumbers = winning.intersect(played)
            .takeUnless { it.isEmpty() }
            ?.size
            ?: return 0
    return 2.0.pow(winningNumbers - 1).roundToInt()
}

generateSequence { readlnOrNull() }
        .map { it.parseCard() }
        .sum()
        .let(::println)

println(start.elapsedNow())