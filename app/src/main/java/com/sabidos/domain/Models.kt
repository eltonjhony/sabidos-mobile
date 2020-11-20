package com.sabidos.domain

data class User(
    val uid: String,
    val isAnonymous: Boolean,
    val phoneNumber: String?,
    val email: String?
)

data class Account(
    val name: String,
    val nickname: String,
    val avatar: UserAvatar?,
    val reputation: Reputation? = null,
    val totalAnswered: Int = 0,
    val totalHits: Int = 0
)

data class UserAvatar(val id: Int, val imageUrl: String?)

data class Reputation(val level: Int, val stars: Int) {
    companion object {
        fun defaultEmpty(): Reputation = Reputation(level = 1, stars = 0)
    }
}

data class WeeklyHits(val date: String, val value: Int?)

data class Timeline(
    val id: Int,
    val description: String,
    val category: Category,
    val date: String,
    val isCorrect: Boolean
)

data class RankingWrapper(
    val podium: List<Ranking>,
    val regularPositions: List<Ranking>
)

data class Ranking(
    val position: Int,
    val avatar: UserAvatar?,
    val firstName: String,
    val nickname: String,
    val reputation: Reputation,
    val totalHits: Int,
    val isMyPosition: Boolean = false
)

data class Category(
    val id: Int,
    val description: String,
    val imageUrl: String?
)

data class Quiz(
    val id: Int,
    val numberOfQuestions: Int,
    val categoryId: Int,
    val questions: List<QuizItem>
)

data class QuizItem(
    val id: Int,
    val position: Int,
    val imageUrl: String?,
    val description: String,
    val quizLimitInSeconds: Int,
    val category: Category,
    val alternatives: List<Alternative>,
    val explanation: Explanation?
) {

    fun getCorrectAnswer(): Alternative {
        return alternatives.first { it.isCorrect }
    }

}

data class Alternative(
    val description: String,
    val isCorrect: Boolean,
    val percentageAnswered: Int
)

data class Explanation(
    val description: String,
    val resource: String
)