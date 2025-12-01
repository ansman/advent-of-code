#!/usr/bin/env kotlin

// Password method 0x434C49434B: count every click that lands on 0 while applying all rotations.

val dialSize = 100L
val start = 50L

fun floorMod(value: Long, mod: Long): Long = ((value % mod) + mod) % mod

// How many steps (clicks) during this rotation land on 0. We check steps 1..distance.
fun zeroHitsDuringRotation(startPos: Long, direction: Long, distance: Long): Long {
    // For right turns, hits occur when k ≡ -start (mod dialSize); for left turns when k ≡ start (mod dialSize).
    val target = if (direction == 1L) (dialSize - (startPos % dialSize)) % dialSize else startPos % dialSize
    val firstHit = if (target == 0L) dialSize else target // k starts at 1, so a remainder of 0 means the 100th click.

    if (firstHit > distance) return 0
    return 1 + (distance - firstHit) / dialSize
}

var position = start
var zeroHits = 0L

generateSequence { readlnOrNull() }
    .filter { it.isNotBlank() }
    .forEach { line ->
        val directionChar = line[0]
        val distance = line.substring(1).trim().toLong()
        val direction = when (directionChar) {
            'L' -> -1L
            'R' -> 1L
            else -> error("Unknown direction: $directionChar")
        }

        zeroHits += zeroHitsDuringRotation(position, direction, distance)
        position = floorMod(position + direction * distance, dialSize)
    }

println(zeroHits)
