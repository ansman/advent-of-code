#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

enum class Direction(val dx: Int, val dy: Int) {
    Right(1, 0),
    Down(0, 1),
    Left(-1, 0),
    Up(0, -1),
}

data class Instruction(
        val direction: Direction,
        val count: Int,
) {
    companion object {
        private val pattern = Regex("""[URDL] \d+ \(#([0-9a-fA-F]{6})\)""")
        fun parse(input: String): Instruction {
            val (color) = pattern.matchEntire(input)!!.destructured
            return Instruction(
                    direction = Direction.entries[color.last().digitToInt()],
                    count = color.dropLast(1).toInt(16)
            )
        }
    }
}

fun List<Pair<Long, Long>>.area(): Long {
    var sum = 0L
    for (i in indices) {
        val (x0, y0) = this[i]
        val (x1, y1) = this[(i + 1) % size]
        sum += x0 * y1 - x1 * y0
    }
    return sum.absoluteValue / 2
}

generateSequence(::readlnOrNull)
        .map(Instruction::parse)
        .onEach { println(it) }
        .fold(mutableListOf(0L to 0L)) { acc, instruction ->
            val (x, y) = acc.last()
            val nx = x + instruction.direction.dx * instruction.count
            val ny = y + instruction.direction.dy * instruction.count
            acc.add(nx to ny)
            acc
        }
        .onEach { println(it) }
        .area()
        .let(::println)


println(start.elapsedNow())