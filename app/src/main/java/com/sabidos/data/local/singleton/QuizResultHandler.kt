package com.sabidos.data.local.singleton

enum class RoundPerformance {
    LOW, MEDIUM, HIGH, PERFECT
}

object QuizResultHandler {

    private var quizResult: QuizResult? = null

    fun init(quizResult: QuizResult) {
        this.quizResult = quizResult
    }

    fun addCorrectAnswer(responseTime: Int) {
        quizResult?.let {
            it.numberOfCorrects += 1
            it.accumulateResponseTime += responseTime
        }
    }

    fun getResults(): QuizResult =
        quizResult ?: throw Error("Make sure to call the init() method first thing")
}

data class QuizResult(
    val categoryId: Int,
    var numberOfCorrects: Int = 0,
    val numberOfQuestions: Int,
    var accumulateResponseTime: Int = 0
) {

    fun getResultPercentageValue(): Int =
        (numberOfQuestions * numberOfCorrects)

    fun getPerformance(): RoundPerformance =
        when ((numberOfCorrects * numberOfQuestions)) {
            100 -> RoundPerformance.PERFECT
            in 80..99 -> RoundPerformance.HIGH
            in 50..79 -> RoundPerformance.MEDIUM
            else -> RoundPerformance.LOW
        }

    fun getAverageResponseTime(): Int =
        accumulateResponseTime / numberOfQuestions

    fun getXPsForRound(): Int =
        numberOfCorrects * 3

}