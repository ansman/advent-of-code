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
val rulesByInput = mutableMapOf<String, MutableList<Rule>>()
val rulesByOutput = mutableMapOf<String, Rule>()

generateSequence { readlnOrNull() }
    .map { Rule.parse(it) }
    .forEach { rule ->
        rulesByInput.getOrPut(rule.left, ::mutableListOf).add(rule)
        rulesByInput.getOrPut(rule.right, ::mutableListOf).add(rule)
        rulesByOutput[rule.output] = rule
    }

fun inputsForNumber(gate: String): String {
    val rule = rulesByOutput[gate] ?: return gate
    var left = inputsForNumber(rule.left)
    var right = inputsForNumber(rule.right)
    val lc = left.count { it == '(' }
    val rc = right.count { it == '(' }
    if (left.length > right.length || left.length == right.length && left < right) {
        val tmp = left
        left = right
        right = tmp
    }
    return "($left ${rule.operation::class.simpleName!!.lowercase()} $right)"
}

fun carry(level: Int): String {
    val l = level.toString().padStart(2, '0')
    return if (level == 0) {
        "(y$l and x$l)"
    } else {
        "((y$l and x$l) or ((y$l xor x$l) and ${carry(level - 1)}))"
    }
}

fun add(level: Int): String {
    val l = level.toString().padStart(2, '0')
    return if (level == 0) {
        "(y$l xor x$l)"
    } else {
        "((y$l xor x$l) xor ${carry(level - 1)})"
    }
}

val zs = rulesByOutput.keys
    .filter { it.startsWith('z') }
    .sorted()

fun getAllInvalid(): List<String> =
    zs.filter {
        val expected = add(it.removePrefix("z").toInt())
        val actual = inputsForNumber(it)
        actual != expected
    }

fun Collection<String>.permutations(): Sequence<Pair<String, String>> = sequence {
    forEachIndexed { i, s ->
        yieldAll(asSequence().drop(i + 1).map { s to it })
    }
}

var invalid = getAllInvalid()

val swaps = mutableSetOf<Pair<String, String>>()
repeat(4) {
    debug("Running iteration $it")
    for ((g1, g2) in rulesByOutput.keys.permutations()) {
        val r1 = rulesByOutput.getValue(g1)
        val r2 = rulesByOutput.getValue(g2)
        rulesByOutput[g1] = r2
        rulesByOutput[g2] = r1
        val i = try {
            getAllInvalid()
        } catch (e: StackOverflowError) {
            invalid
        }
        if (i.size < invalid.size) {
            invalid = i
            swaps.add(g1 to g2)
            return@repeat
        } else {
            rulesByOutput[g1] = r1
            rulesByOutput[g2] = r2
        }
    }
}

println(swaps.flatMap { it.toList() }.sorted().joinToString(","))


println("Ran in ${startTime.elapsedNow()}")