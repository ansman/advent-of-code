#!/usr/bin/env kotlin

var dial = 50
var timesAtZero = 0
generateSequence { readlnOrNull() }
    .map { op ->
        var dir = if (op[0] == 'L') -1 else 1
        op.drop(1).toInt() * dir
    }
    .forEach { add ->
        dial = ((dial + add) % 100 + 100) % 100
        if (dial == 0) {
            ++timesAtZero
        }
    }
println(timesAtZero)