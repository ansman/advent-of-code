#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.math.min

val pattern = Regex("""Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.+)""")

data class Valve(
        val id: String,
        val rate: Int,
        val pathsToOtherValves: List<String>
)

val valves = mutableMapOf<String, Valve>()
var nonEmptyValveCount = 0

while (true) {
    val line = readLine() ?: break
    val (id, rate, paths) = pattern.matchEntire(line)?.destructured
            ?: throw IllegalArgumentException("Invalid line '$line'")
    val valve = Valve(
            id = id,
            rate = rate.toInt(),
            pathsToOtherValves = paths.split(", ")
    )
    valves[id] = valve
    if (valve.rate > 0) {
        ++nonEmptyValveCount
    }
}

data class Key(
        val currentPosition: String,
        val openValves: Set<String>,
        val minutesRemaining: Int,
        val isElephant: Boolean,
)

fun bestCase(
        currentPosition: String = "AA",
        isElephant: Boolean = false,
        openValves: Set<String> = emptySet(),
        minutesRemaining: Int = 26,
        memo: MutableMap<Key, Int> = mutableMapOf(),
): Int {
    if (nonEmptyValveCount == openValves.size || minutesRemaining <= 1) return 0
    val key = Key(currentPosition, openValves, minutesRemaining, isElephant)
    memo[key]?.let { return it }

    val valve = valves.getValue(currentPosition)
    fun tryAllPaths(mr: Int = minutesRemaining, ov: Set<String> = openValves): Int =
            valve.pathsToOtherValves.maxOf {
                bestCase(
                        currentPosition = it,
                        openValves = ov,
                        minutesRemaining = mr - 1,
                        isElephant = isElephant,
                        memo = memo,
                )
            }

    var best = tryAllPaths()
    if (valve.rate > 0 && valve.id !in openValves) {
        best = max(
                best,
                (minutesRemaining - 1) * valve.rate + tryAllPaths(
                        mr = minutesRemaining - 1,
                        ov = openValves + currentPosition
                )
        )
    }

    // Try elephant
    if (openValves.isNotEmpty() && !isElephant) {
        best = max(
                best,
                bestCase(
                        openValves = openValves,
                        isElephant = true,
                        memo = memo
                )
        )
    }

    memo[key] = best
    return best
}
println(bestCase())