#!/usr/bin/env kotlin

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

fun List<Map<String, Int>>.isGamePossible(): Boolean = all { turn ->
    turn.getOrDefault("red", 0) <= 12 &&
        turn.getOrDefault("green", 0) <= 13 &&
        turn.getOrDefault("blue", 0) <= 14
}

generateSequence { readlnOrNull() }
    .map { it.parseLine() }
    .filter { it.second.isGamePossible() }
    .map { it.first }
    .sum()
    .let { println(it) }