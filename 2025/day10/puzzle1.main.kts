#!/usr/bin/env kotlin

import java.util.*
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

data class Computer(
    val lights: List<Boolean>,
    val buttons: List<Set<Int>>,
    val joltage: Set<Int>
) {
    fun computeMinPresses(): Int {
        data class Run(val lights: List<Boolean>, val presses: Int)

        val queue = LinkedList<Run>()
        queue.add(Run(lights.map { false }, 0))
        val seen = mutableSetOf(queue.first().lights)
        while (true) {
            val run = queue.removeFirst()
            for (button in buttons) {
                val newLights = run.lights.toMutableList()
                for (index in button) {
                    newLights[index] = !newLights[index]
                }
                if (newLights == lights) {
                    return run.presses + 1
                }
                if (seen.add(newLights)) {
                    queue.add(Run(newLights, run.presses + 1))
                }
            }
        }
    }

    companion object {
        private val pattern = """\[(.+)\] (.+) \{(.+)\}""".toRegex()

        fun parse(input: String): Computer {
            val (lights, buttons, joltage) = pattern.matchEntire(input)!!.destructured
            return Computer(
                lights = lights.map { it == '#' },
                buttons = buttons.split(" ").map { button ->
                    button.removeSurrounding("(", ")")
                        .split(",")
                        .map(String::toInt)
                        .toSet()
                },
                joltage = joltage.split(",").map(String::toInt).toSet()
            )
        }
    }
}

val sum = generateSequence { readlnOrNull() }
    .map(Computer::parse)
    .map(Computer::computeMinPresses)
    .sum()

println(sum)

println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")