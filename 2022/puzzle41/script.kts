#!/usr/bin/env kotlin


import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: Any?) {
    if (logging) {
        println(msg)
    }
}

sealed class Statement {
    companion object {
        private val operationPattern = Regex("""(.+) ([+*/-]) (.+)""")
        fun parse(input: String): Statement =
                operationPattern.matchEntire(input)
                        ?.destructured
                        ?.let { (left, operator, right) ->
                            Operation(parse(left), Operation.Operator.fromSymbol(operator), parse(right))
                        }
                        ?: input.toLongOrNull()?.let(::Constant)
                        ?: Reference(input)
    }

    data class Constant(val value: Long) : Statement()
    data class Reference(val name: String) : Statement()
    data class Operation(val left: Statement, val operator: Operator, val right: Statement) : Statement() {
        enum class Operator(val symbol: String, val operation: (Long, Long) -> Long) {
            Plus("+", Long::plus),
            Minus("-", Long::minus),
            Times("*", Long::times),
            Division("/", Long::div);

            companion object {
                fun fromSymbol(symbol: String) = requireNotNull(values().firstOrNull { it.symbol == symbol }) {
                    "Unknown symbol $symbol"
                }
            }
        }
    }
}

data class Monkey(
        val name: String,
        val statement: Statement,
)

val monkeys = buildMap {
    while (true) {
        val (name, statement) = readLine()?.split(": ") ?: break
        put(name, Statement.parse(statement))
    }
}

fun Statement.execute(): Long =
        when (this) {
            is Statement.Constant -> value
            is Statement.Reference -> monkeys.getValue(name).execute()
            is Statement.Operation -> operator.operation(left.execute(), right.execute())
        }

println("Monkey root will yell ${monkeys.getValue("root").execute()}")