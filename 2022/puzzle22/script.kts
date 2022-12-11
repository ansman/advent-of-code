#!/usr/bin/env kotlin

sealed class Operand {
    abstract fun getValue(previous: Long): Long

    companion object {
        fun parse(input: String): Operand =
                when (input) {
                    "old" -> Previous
                    else -> Constant(input.toLong())
                }
    }

    data class Constant(val value: Long) : Operand() {
        override fun getValue(previous: Long): Long = value
    }

    object Previous : Operand() {
        override fun getValue(previous: Long): Long = previous
    }
}

sealed class Operation {
    abstract fun perform(previous: Long): Long

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
        override fun perform(previous: Long): Long = left.getValue(previous) + right.getValue(previous)
    }

    data class Multiply(val left: Operand, val right: Operand) : Operation() {
        override fun perform(previous: Long): Long = left.getValue(previous) * right.getValue(previous)
    }
}

data class Monkey(
        val items: MutableList<Long>,
        val operation: Operation,
        val test: Long,
        val ifTrue: Int,
        val ifFalse: Int,
) {
    var inspectCount = 0L
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
            .toLong()
    val items = readLine()!!
            .removeRequiredPrefix("  Starting items: ")
            .splitToSequence(", ")
            .map { it.toLong() }
            .toMutableList()
    val operation = readLine()!!
            .removeRequiredPrefix("  Operation: new = ")
            .let(Operation::parse)
    val test = readLine()!!
            .removeRequiredPrefix("  Test: divisible by ")
            .toLong()
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

val divisor: Long = monkeys.fold(1L) { acc, monkey -> acc * monkey.test }

repeat(10000) { round ->
    for (monkey in monkeys) {
        val it = monkey.items.iterator()
        while (it.hasNext()) {
            val transformed = monkey.operation.perform(it.next()) % divisor
            val target = if (transformed % monkey.test == 0L) {
                monkeys[monkey.ifTrue]
            } else {
                monkeys[monkey.ifFalse]
            }
            target.items.add(transformed)
            it.remove()
            ++monkey.inspectCount
        }
    }
    when (round + 1) {
        1,
        20,
        1000,
        2000,
        3000,
        4000,
        5000,
        6000,
        8000,
        9000,
        10000 -> {
            println("== After round ${round + 1} ==")
            monkeys.forEachIndexed { index, monkey ->
                println("Monkey $index inspected items ${monkey.inspectCount} times.")
            }
            println()
        }
    }
}

println(monkeys
        .sortedByDescending { it.inspectCount }
        .take(2)
        .map { it.inspectCount }
        .reduce(Long::times))
