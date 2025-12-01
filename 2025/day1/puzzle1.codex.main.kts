#!/usr/bin/env kotlin

// Counts how many times the dial lands on 0 while applying all rotations.

val DIAL_SIZE = 100
val START = 50

fun floorMod(value: Int, mod: Int): Int = ((value % mod) + mod) % mod

var position = START
var hitsOnZero = 0

generateSequence { readlnOrNull() }
    .filter { it.isNotBlank() }
    .forEach { line ->
        val direction = line[0]
        val distance = line.substring(1).trim().toInt()

        position = when (direction) {
            'L' -> floorMod(position - distance, DIAL_SIZE)
            'R' -> floorMod(position + distance, DIAL_SIZE)
            else -> error("Unknown direction: $direction")
        }

        if (position == 0) hitsOnZero++
    }

println(hitsOnZero)
