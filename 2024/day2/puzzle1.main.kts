#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.sign

generateSequence { readlnOrNull() }
    .map { line -> line.split(" ").map { it.toInt() } }
    .filter { report ->
        val isIncreasing = (report[1] - report[0]).sign
        report
            .zipWithNext()
            .all { (a, b) ->
                (b - a).absoluteValue in 1..3 && (b - a).sign == isIncreasing
            }
    }
    .count()
    .let { println(it) }