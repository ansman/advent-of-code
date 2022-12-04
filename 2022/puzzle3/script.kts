#!/usr/bin/env kotlin

enum class Hand(val value: Int) {
    Rock(1),
    Paper(2),
    Scissors(3);

    val winsAgainst: Hand
        get() = when (this) {
            Rock -> Scissors
            Paper -> Rock
            Scissors -> Paper
        }

    val losesAgainst: Hand
        get() = when (this) {
            Rock -> Paper
            Paper -> Scissors
            Scissors -> Rock
        }

    fun scoreAgainst(myHand: Hand): Int =
            when {
                this == myHand -> SCORE_TIE
                myHand == this.winsAgainst -> SCORE_LOSE
                else -> SCORE_WIN
            }

    companion object {
        private const val SCORE_LOSE = 0
        private const val SCORE_TIE = 3
        private const val SCORE_WIN = 6

        fun parse(s: String): Hand =
                when (s) {
                    "A", "X" -> Rock
                    "B", "Y" -> Paper
                    "C", "Z" -> Scissors
                    else -> error("Unknown hand $s")
                }
    }
}

var score = 0L
while (true) {
    val line = readLine() ?: break
    val (opponent, mine) = line.split(" ").map(Hand::parse)
    score += opponent.scoreAgainst(mine) + mine.value
}
println(score)