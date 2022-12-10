#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.sign

val registers = mutableMapOf<String, Int>().withDefault { 1 }

sealed class Operation {
    object Noop : Operation()
    data class Add(val value: Int, val register: String = "x") : Operation()
}

var cycle = 1
val operationQueue = ArrayDeque<Operation>()

fun step() {
    ++cycle
    when (val operation = operationQueue.removeFirstOrNull() ?: Operation.Noop) {
        Operation.Noop -> {}
        is Operation.Add -> {
            registers[operation.register] = registers.getValue(operation.register) + operation.value
        }
    }
}

fun readOperations(): List<Operation> {
    val line = readLine() ?: return emptyList()
    val parts = line.split(" ")
    return when (parts[0]) {
        "noop" -> listOf(Operation.Noop)
        "addx" -> listOf(Operation.Noop, Operation.Add(parts[1].toInt(), "x"))
        else -> error("Unknown operation $line")
    }
}

val width = 40
val height = 6
var x = 0
var y = 0

fun renderPixel() {
    val sprite = registers.getValue("x")
    print(if ((sprite - x).absoluteValue <= 1) {
        '#'
    } else {
        ' '
    })
    ++x
    if (x == width) {
        println()
        x = 0
        ++y
    }
    if (y == height) {
        println()
        y = 0
    }
}

var signalStrength = 0
while (true) {
    operationQueue.addAll(readOperations())
    if (operationQueue.isEmpty()) break
    renderPixel()
    step()
}
