package com.sabidos.data.local.singleton

object QuizResultHandler {

    private var quizResult: QuizResult? = null

    fun init(quizResult: QuizResult) {
        this.quizResult = quizResult
    }

    fun addCorrectAnswer() {
        quizResult?.let {
            it.numberOfCorrects += 1
        }
    }

    fun getResults(): QuizResult =
        quizResult ?: throw Error("Make sure to call the init() method first thing")
}

data class QuizResult(
    val categoryId: Int,
    var numberOfCorrects: Int = 0,
    val numberOfQuestions: Int
) {

    fun getResultPercentage(): String {
        val value = getResultPercentageValue().toInt()
        return "$value%"
    }

    fun getResultPercentageValue(): Float =
        (numberOfQuestions * numberOfCorrects).toFloat()

}