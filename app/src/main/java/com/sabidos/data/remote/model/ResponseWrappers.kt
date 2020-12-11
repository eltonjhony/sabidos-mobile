package com.sabidos.data.remote.model

// Response objects

data class AccountResponseWrapper(
    val account: AccountResponse?
)

data class AccountResponse(
    val name: String,
    val nickname: String,
    val avatar: UserAvatarResponse?,
    val reputation: ReputationResponse,
    val totalAnswered: Int,
    val totalHits: Int
)

data class QuizResponseWrapper(
    val id: Int,
    val categoryId: Int,
    val numberOfQuestions: Int,
    val questions: List<QuizResponse>
)

data class QuizResponse(
    val id: Int,
    val imageUrl: String?,
    val description: String,
    val quizLimitInSeconds: Int,
    val category: CategoryResponse,
    val alternatives: List<AlternativeResponse>,
    val explanation: ExplanationResponse?
)

data class AlternativeResponse(
    val description: String,
    val isCorrect: Boolean,
    val percentageAnswered: Int
)

data class ExplanationResponse(
    val description: String,
    val resource: String
)

data class AvatarWrapperResponse(val avatars: List<UserAvatarResponse>)
data class UserAvatarResponse(val id: Int, val imageUrl: String?)

data class ReputationResponse(val level: Int, val stars: Int)

data class WeeklyHitsWrapperResponse(val hits: List<WeeklyHitsResponse>)
data class WeeklyHitsResponse(val date: String, val value: Int?)

data class TimelineWrapperResponse(
    val page: Int,
    val pageSize: Int,
    val questions: List<TimelineResponse>
)

data class TimelineResponse(
    val id: Int,
    val description: String,
    val category: CategoryResponse,
    val date: String,
    val isCorrect: Boolean
)

data class RankingWrapperResponse(
    val podium: List<RankingResponse>,
    val regularPositions: List<RankingResponse>
)

data class RankingResponse(
    val position: Int,
    val avatar: UserAvatarResponse?,
    val player: PlayerResponse,
    val totalHits: Int,
    val isMyPosition: Boolean
)

data class PlayerResponse(
    val firstName: String,
    val nickname: String,
    val reputation: ReputationResponse
)

data class CategoryWrapperResponse(val categories: List<CategoryResponse>)
data class CategoryResponse(val id: Int, val description: String, val imageUrl: String?, val iconUrl: String?)