#!/usr/bin/env kotlin

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: Any?) = log { msg }
fun log(msg: () -> Any?) {
    if (logging) {
        println(msg())
    }
}

fun MutableList<Int>.getOrZero(i: Int): Int {
    while (i >= size) {
        add(0)
    }
    return this[i]
}

val output = mutableListOf<Int>()

fun snafuToDecimal(number: Char) =
        when (number) {
            '-' -> -1
            '=' -> -2
            else -> number.digitToInt()
        }

fun snafuToDecimal(number: String): Long = snafuToDecimal(number.map(::snafuToDecimal).asReversed())

fun snafuToDecimal(number: List<Int>): Long {
    var output = 0L
    number.forEachIndexed { index, n ->
        output += n * (5.0.pow(index)).roundToLong()
    }
    return output
}

while (true) {
    val line = readlnOrNull() ?: break
    log { "Got $line (${snafuToDecimal(line)})" }
    line.reversed().forEachIndexed { i, c ->
        output[i] = output.getOrZero(i) + snafuToDecimal(c)
    }
    log { "  Sum is ${snafuToDecimal(output)}" }
}

while (output.last() == 0) {
    output.removeLast()
}

var i = 0
while (i < output.size) {
    var v = output[i]
    var carry = 0
    while (v < -2) {
        v += 5
        carry -= 1
    }
    while (v > 2) {
        v -= 5
        carry += 1
    }
    output[i] = v
    if (carry != 0)
    output[i + 1] = output.getOrZero(i + 1) + carry
    ++i
}

while (output.last() == 0) {
    output.removeLast()
}
output.asReversed().forEach {
    print(when (it) {
        -2 -> '='
        -1 -> '-'
        else -> it.toString()
    })
}
println()
