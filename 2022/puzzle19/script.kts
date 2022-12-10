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

var signalStrength = 0
while (true) {
    operationQueue.addAll(readOperations())
    if (operationQueue.isEmpty()) break
    if ((cycle - 20) % 40 == 0 && cycle <= 220) {
        signalStrength += cycle * registers.getValue("x")
    }
    step()
}
println(signalStrength)
