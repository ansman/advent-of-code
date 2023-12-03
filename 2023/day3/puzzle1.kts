#!/usr/bin/env kotlin

val grid = generateSequence { readlnOrNull() }
    .toList()

val Char.isSymbol: Boolean
    get() = this != '.' && !isDigit()

val symbols = grid.map { row ->
    BooleanArray(row.length) { i -> row[i].isSymbol }
}

fun List<BooleanArray>.isPartAdjacent(row: Int, start: Int, end: Int): Boolean {
    for (ri in (row - 1)..(row + 1)) {
        val r = getOrNull(ri) ?: continue
        for (c in (start - 1)..(end + 1)) {
            if (ri == row && c in start..end) continue
            if (r.getOrNull(c) == true) return true
        }
    }
    return false
}

val numberPattern = Regex("\\d+")
grid.asSequence()
    .flatMapIndexed { row: Int, line: String ->
        numberPattern.findAll(line)
            .filter{ symbols.isPartAdjacent(row, it.range.first, it.range.last) }
            .map { it.value.toInt() }
    }
    .sum()
    .let { println(it) }