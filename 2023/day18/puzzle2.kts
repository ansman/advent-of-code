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
    var interiorArea = 0L
    for (i in indices) {
        val (x0, y0) = this[i]
        val (x1, y1) = this[(i + 1) % size]
        interiorArea += (y0 + y1) * (x0 - x1)
    }
    interiorArea = interiorArea.absoluteValue / 2

    val border = asSequence()
        .zipWithNext()
        .fold(0L) { acc, (p0, p1) ->
            acc + (p0.first - p1.first).absoluteValue + (p0.second - p1.second).absoluteValue
        }

    return interiorArea + border / 2 + 1
}

val instructions = generateSequence(::readlnOrNull)
    .map(Instruction::parse)
    .fold(mutableListOf(0L to 0L)) { acc, instruction ->
        val (x, y) = acc.last()
        val nx = x + instruction.direction.dx * instruction.count
        val ny = y + instruction.direction.dy * instruction.count
        acc.add(nx to ny)
        acc
    }
    .area()
    .let(::println)


println(start.elapsedNow())