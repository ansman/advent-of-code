#!/usr/bin/env kotlin

import kotlin.math.absoluteValue

var dial = 50
var timesAtZero = 0
generateSequence { readlnOrNull() }
    .map { op ->
        var dir = if (op[0] == 'L') -1 else 1
        op.drop(1).toInt() * dir
    }
    .forEach { add ->
        timesAtZero += add.absoluteValue / 100
        val prev = dial
        dial = ((dial + add) % 100 + 100) % 100
        if (add < 0 && prev < dial || add > 0 && prev > dial) {
            ++timesAtZero
        }
    }
println(timesAtZero)