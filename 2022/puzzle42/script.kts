#!/usr/bin/env kotlin


import Script.Statement.Operation.Operator
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

val me = "humn"

fun Statement.execute(): Long =
        when (this) {
            is Statement.Constant -> value
            is Statement.Reference -> {
                require(name != me) { "$me cannot be resolved" }
                monkeys.getValue(name).execute()
            }

            is Statement.Operation -> operator.operation(left.execute(), right.execute())
        }

fun Statement.simplify(): Statement =
        when (this) {
            is Statement.Constant -> this
            is Statement.Reference -> if (name == me) this else monkeys.getValue(name).simplify()
            is Statement.Operation -> {
                val left = left.simplify()
                val right = right.simplify()
                if (left is Statement.Constant && right is Statement.Constant) {
                    Statement.Constant(execute())
                } else {
                    copy(left = left, right = right)
                }
            }
        }

fun Statement.toDebugString(): String =
        when (this) {
            is Statement.Constant -> value.toString()
            is Statement.Reference -> if (name == me) name else monkeys.getValue(name).toDebugString()
            is Statement.Operation -> "(${left.toDebugString()} ${operator.symbol} ${right.toDebugString()})"
        }

val rootOperation = monkeys.getValue("root") as Statement.Operation
lateinit var statement: Statement
var constant: Long = 0
run {
    val l = rootOperation.left.simplify()
    val r = rootOperation.right.simplify()
    if (l is Statement.Constant) {
        constant = l.value
        statement = r
    } else if (r is Statement.Constant) {
        statement = l
        constant = r.value
    } else {
        error("Cannot compute statement when reference is on both sides...")
    }
}

var target = Statement.Reference(me)
while (true) {
    when (val s = statement) {
        is Statement.Constant -> error("Left should not be a constant")
        is Statement.Reference -> {
            require(s.name == me) { "Wrong reference reached" }
            break
        }

        is Statement.Operation -> {
            val left = s.left.simplify()
            val right = s.right.simplify()

            when (s.operator) {
                Operator.Plus ->
                    if (left is Statement.Constant) {
                        statement = right
                        constant -= left.value
                    } else if (right is Statement.Constant) {
                        statement = left
                        constant -= right.value
                    } else {
                        error("Reference on both sides of statement isn't supported")
                    }

                Operator.Minus -> {
                    if (left is Statement.Constant) {
                        statement = right
                        constant = left.value - constant
                    } else if (right is Statement.Constant) {
                        statement = left
                        constant += right.value
                    } else {
                        error("Reference on both sides of statement isn't supported")
                    }
                }
                Operator.Times -> {
                    if (left is Statement.Constant) {
                        statement = right
                        constant /= left.value
                    } else if (right is Statement.Constant) {
                        statement = left
                        constant /= right.value
                    } else {
                        error("Reference on both sides of statement isn't supported")
                    }
                }
                Operator.Division -> {
                    if (left is Statement.Constant) {
                        statement = right
                        constant = left.value / constant
                    } else if (right is Statement.Constant) {
                        statement = left
                        constant *= right.value
                    } else {
                        error("Reference on both sides of statement isn't supported")
                    }
                }
            }
        }
    }
}
println("$me is equal to $constant")