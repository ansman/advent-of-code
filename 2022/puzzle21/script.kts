#!/usr/bin/env kotlin

sealed class Operand {
    abstract fun getValue(previous: Int): Int

    companion object {
        fun parse(input: String): Operand =
                when (input) {
                    "old" -> Previous
                    else -> Constant(input.toInt())
                }
    }

    data class Constant(val value: Int) : Operand() {
        override fun getValue(previous: Int): Int = value
    }

    object Previous : Operand() {
        override fun getValue(previous: Int): Int = previous
    }
}

sealed class Operation {
    abstract fun perform(previous: Int): Int

    companion object {
        fun parse(input: String): Operation {
            val (left, operator, right) = input.split(" ")
            return when (operator) {
                "+" -> Add(Operand.parse(left), Operand.parse(right))
                "*" -> Multiply(Operand.parse(left), Operand.parse(right))
                else -> error("Unknown operation $input")
            }
        }
    }

    data class Add(val left: Operand, val right: Operand) : Operation() {
        override fun perform(previous: Int): Int = left.getValue(previous) + right.getValue(previous)
    }

    data class Multiply(val left: Operand, val right: Operand) : Operation() {
        override fun perform(previous: Int): Int = left.getValue(previous) * right.getValue(previous)
    }
}

data class Monkey(
        val items: MutableList<Int>,
        val operation: Operation,
        val test: Int,
        val ifTrue: Int,
        val ifFalse: Int,
) {
    var inspectCount = 0
}

fun String.removeRequiredPrefix(prefix: String): String {
    require(startsWith(prefix))
    return removePrefix(prefix)
}

val monkeys = mutableListOf<Monkey>()

val pattern1 = Regex("""Monkey (\d+):""")
fun parseMonkey(): Monkey? {
    val line = readLine() ?: return null
    val number = pattern1.matchEntire(line)!!
            .groups[1]!!
            .value
            .toInt()
    val items = readLine()!!
            .removeRequiredPrefix("  Starting items: ")
            .splitToSequence(", ")
            .map { it.toInt() }
            .toMutableList()
    val operation = readLine()!!
            .removeRequiredPrefix("  Operation: new = ")
            .let(Operation::parse)
    val test = readLine()!!
            .removeRequiredPrefix("  Test: divisible by ")
            .toInt()
    val ifTrue = readLine()!!
            .removeRequiredPrefix("    If true: throw to monkey ")
            .toInt()
    val ifFalse = readLine()!!
            .removeRequiredPrefix("    If false: throw to monkey ")
            .toInt()
    // Consumes empty line
    readLine()
    return Monkey(
            items = items,
            operation = operation,
            test = test,
            ifTrue = ifTrue,
            ifFalse = ifFalse,
    )
}

while (true) {
    monkeys.add(parseMonkey() ?: break)
}
println("Got ${monkeys.size} monkeys")

repeat(20) {
    for (monkey in monkeys) {
        val it = monkey.items.iterator()
        while (it.hasNext()) {
            val transformed = monkey.operation.perform(it.next()) / 3
            val target = if (transformed % monkey.test == 0) {
                monkeys[monkey.ifTrue]
            } else {
                monkeys[monkey.ifFalse]
            }
            target.items.add(transformed)
            it.remove()
            ++monkey.inspectCount
        }
    }
    monkeys.forEachIndexed { index, monkey ->
        println("Monkey $index: ${monkey.items.joinToString(", ")}")
    }
    println()
}

println(monkeys
        .sortedByDescending { it.inspectCount }
        .take(2)
        .map { it.inspectCount }
        .reduce(Int::times))
