#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

data class Rule(
    val left: String,
    val right: String,
    val operation: Operation,
    val output: String,
) {
    fun apply(left: Boolean, right: Boolean): Boolean = operation(left, right)

    override fun toString(): String = "$left ${operation::class.simpleName!!.uppercase()} $right -> $output"

    companion object {
        fun parse(line: String): Rule {
            val (statement, output) = line.split(" -> ")
            val (left, op, right) = statement.split(" ")
            val operation = when (op) {
                "AND" -> Operation.And
                "OR" -> Operation.Or
                "XOR" -> Operation.Xor
                else -> error("Unknown operation: $op")
            }
            return Rule(left, right, operation, output)
        }
    }

    sealed class Operation {
        abstract operator fun invoke(left: Boolean, right: Boolean): Boolean

        data object And : Operation() {
            override fun invoke(left: Boolean, right: Boolean): Boolean = left && right
        }

        data object Or : Operation() {
            override fun invoke(left: Boolean, right: Boolean): Boolean = left || right
        }

        data object Xor : Operation() {
            override fun invoke(left: Boolean, right: Boolean): Boolean = left != right
        }
    }
}

val initialStates = generateSequence { readln().ifEmpty { null } }
    .map { l -> l.split(": ") }
    .toList()
    .associateBy({ it[0] }, { it[1] == "1" })

val gates = mutableMapOf<String, Boolean>()
val rules = mutableMapOf<String, MutableList<Rule>>()

fun setGate(name: String, value: Boolean) {
    gates[name] = value
    rules[name]?.forEach { rule ->
        val left = gates[rule.left] ?: return@forEach
        val right = gates[rule.right] ?: return@forEach
        val output = rule.apply(left, right)
        val l = if (left) "1" else "0"
        val r = if (right) "1" else "0"
        val o = if (output) "1" else "0"
        debug("Executing ${rule.left} ($l) ${rule.operation.javaClass.simpleName.uppercase()} ${rule.right} ($r) -> ${rule.output} ($o)")
        setGate(rule.output, output)
    }
}

generateSequence { readlnOrNull() }
    .map { Rule.parse(it) }
    .forEach { rule ->
        rules.getOrPut(rule.left, ::mutableListOf).add(rule)
        rules.getOrPut(rule.right, ::mutableListOf).add(rule)
    }

initialStates.forEach { (gate, value) -> setGate(gate, value) }

gates.entries
    .asSequence()
    .filter { it.key.startsWith('z') }
    .sortedByDescending { it.key }
    .map { if (it.value) "1" else 0 }
    .joinToString("")
    .also { debug(it) }
    .toLong(2)
    .let { println(it) }

println("Ran in ${startTime.elapsedNow()}")