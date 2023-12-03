#!/usr/bin/env kotlin

val grid = generateSequence { readlnOrNull() }
    .toList()

val numberPattern = Regex("\\d+")
val numbers = grid.mapIndexed { row: Int, line: String ->
    numberPattern.findAll(line)
        .map { it.range to it.value.toInt() }
        .toList()
}

fun List<List<Pair<IntRange, Int>>>.findAdjacent(row: Int, col: Int): List<Int> {
    val adjacent = ((row - 1)..(row + 1))
        .flatMap { r -> ((col - 1)..(col + 1)).map { c -> r to c } }
        .toSet()

    return ((row - 1)..(row + 1)).flatMap { r ->
        this[r]
            .filter { (range, _) -> range.any { c -> r to c in adjacent } }
            .map { it.second }
    }
}

grid.asSequence()
    .flatMapIndexed { r: Int, row: String ->
        row.asSequence()
            .mapIndexed { i, c -> i to c }
            .filter { it.second == '*' }
            .map { it.first }
            .map { c -> numbers.findAdjacent(r, c) }
            .filter { it.size == 2 }
            .map { it.reduce { a, b -> a * b } }
    }
    .sum()
    .let(::println)
