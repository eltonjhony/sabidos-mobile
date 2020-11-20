package com.sabidos.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.data.local.singleton.QuizResult
import com.sabidos.data.remote.model.FinishRoundRequest
import com.sabidos.domain.interactor.PostRoundUseCase
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val postRoundUseCase: PostRoundUseCase
) : ViewModel() {

    fun postRound(roundId: Int, quizResult: QuizResult) {

        val request = FinishRoundRequest(
            roundId,
            quizResult.getAverageResponseTime(),
            quizResult.getXPsForRound()
        )

        viewModelScope.launch {
            postRoundUseCase(PostRoundUseCase.Params(request)) {
                Logger.withTag(ResultsViewModel::class.java.simpleName).i("Post round returned: $it")
            }
        }
    }

}