#!/usr/bin/env kotlin

import kotlin.math.max

fun String.parseTurn(): Map<String, Int> = buildMap {
    splitToSequence(", ")
        .map { it.split(" ") }
        .forEach { (count, color) ->
            put(color, count.toInt())
        }
}

fun String.parseLine(): Pair<Int, List<Map<String, Int>>> {
    val (game, turns) = split(": ")
    return game.removePrefix("Game ").toInt() to turns
        .splitToSequence("; ")
        .map { it.parseTurn() }
        .toList()
}

fun List<Map<String, Int>>.getMinimumPossibleCubes(): Map<String, Int> = reduce { acc, map ->
    acc.toMutableMap().apply {
        map.forEach { (color, count) ->
            put(color, max(getOrDefault(color, 0), count))
        }
    }
}

fun Map<String, Int>.computePower(): Int = values.fold(1) { acc, i -> acc * i }

generateSequence { readlnOrNull() }
    .map { it.parseLine() }
    .map { it.second }
    .map { it.getMinimumPossibleCubes() }
    .map { it.computePower() }
    .sum()
    .let { println(it) }