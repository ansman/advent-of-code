#!/usr/bin/env kotlin

val lookup = listOf(
    "1" to "one",
    "2" to "two",
    "3" to "three",
    "4" to "four",
    "5" to "five",
    "6" to "six",
    "7" to "seven",
    "8" to "eight",
    "9" to "nine",
)

fun String.startingDigit(start: Int): Int? {
    lookup.forEachIndexed { number, (digit, word) ->
        when {
            startsWith(digit, startIndex = start) -> return number + 1
            startsWith(word, startIndex = start) -> return number + 1
        }
    }
    return null
}

fun String.mapToDigits(): List<Int> = buildList {
    var start = 0
    while (start < length) {
        startingDigit(start)?.let(::add)
        ++start
    }
}

generateSequence { readlnOrNull() }
        .map { it.mapToDigits() }
        .map { "${it.first()}${it.last()}" }
        .map { it.toInt() }
        .sum()
        .let { println(it) }