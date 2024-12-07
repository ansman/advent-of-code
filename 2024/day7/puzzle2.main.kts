#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = true
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

data class Calibration(
    val value: Long,
    val numbers: List<Long>
) {
    fun isValid(): Boolean = sums().any { it == value }

    private fun sums(index: Int = numbers.lastIndex): Sequence<Long> = sequence {
        val number = numbers[index]
        if (index == 0) {
            yield(number)
            return@sequence
        }
        for (sum in sums(index - 1)) {
            for (op in operations) {
                yield(op(sum, number))
            }
        }
    }

    companion object {
        private val operations: List<(Long, Long) -> Long> = listOf(
            Long::times,
            Long::plus,
            { a, b -> (a.toString() + b.toString()).toLong() }
        )
        fun parse(line: String): Calibration {
            val (value, numbers) = line.split(": ")
            return Calibration(value.toLong(), numbers.split(' ').map { it.toLong() })
        }
    }
}

val start = TimeSource.Monotonic.markNow()
generateSequence { readlnOrNull() }
    .map(Calibration::parse)
    .filter { it.isValid() }
    .sumOf { it.value }
    .let { println("Sum is $it") }

println("Ran in ${start.elapsedNow()}")