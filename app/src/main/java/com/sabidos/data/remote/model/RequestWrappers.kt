package com.sabidos.data.remote.model

import com.sabidos.domain.Alternative

// Request objects
data class AccountRequest(
    val name: String = "",
    val nickname: String = "",
    val defaultAvatarId: Int? = null
) {
    var uid: String? = null
    var isAnonymous: Boolean? = null
    var email: String? = null
    var phone: String? = null
}

data class QuizRequest(
    val quizId: Int,
    val responseTime: Int,
    val alternative: Alternative
)

data class FinishRoundRequest(
    val accumulateXp: Int
)