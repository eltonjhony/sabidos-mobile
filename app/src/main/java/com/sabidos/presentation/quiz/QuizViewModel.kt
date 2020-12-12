package com.sabidos.presentation.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.data.local.singleton.QuizResult
import com.sabidos.data.local.singleton.QuizResultHandler
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Alternative
import com.sabidos.domain.Quiz
import com.sabidos.domain.QuizItem
import com.sabidos.domain.interactor.GetNextRoundUseCase
import com.sabidos.domain.interactor.GetNextRoundUseCase.Params
import com.sabidos.domain.interactor.PostQuizUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.*
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.launch

class QuizViewModel(
    private val getNextRoundUseCase: GetNextRoundUseCase,
    private val postQuizUseCase: PostQuizUseCase
) : ViewModel() {

    val roundResource = MutableLiveData<Resource<Quiz>>()
    val quizItemResource = MutableLiveData<Resource<QuizItem>>()
    val currentQuizResource = MutableLiveData<Resource<Pair<Int, Int>>>()

    private val roundQuizList = mutableListOf<QuizItem>()

    private var roundTotal: Int? = null

    private var roundPosition = 0

    private var choiceCategoryId: Int? = null

    fun getNewRoundFor(categoryId: Int?) {
        choiceCategoryId = categoryId
        roundResource.loading()
        viewModelScope.launch {
            getNextRoundUseCase(Params(categoryId ?: DEFAULT_CATEGORY_FALLBACK)) {
                when (it) {
                    is ResultWrapper.Success -> handleSuccessRound(it.data)
                    is ResultWrapper.NetworkError -> roundResource.setNetworkFailure()
                    else -> roundResource.setGenericFailure()
                }
            }
        }

    }

    fun getNextQuizForRound() {
        if (roundQuizList.isNullOrEmpty()) {
            roundResource.setFinish()
        } else {
            val nextQuiz = roundQuizList.first()
            roundQuizList.remove(nextQuiz)
            roundPosition++
            roundTotal?.let { currentQuizResource.setSuccess(Pair(roundPosition, it)) }
            quizItemResource.setSuccess(nextQuiz)
        }
    }

    private fun handleSuccessRound(quiz: Quiz) {
        QuizResultHandler.init(
            QuizResult(categoryId = choiceCategoryId, numberOfQuestions = quiz.numberOfQuestions)
        )
        roundQuizList.addAll(quiz.questions)
        roundTotal = quiz.numberOfQuestions
        roundPosition = 0
        roundResource.setSuccess(quiz)
    }

    fun postQuiz(quizItem: QuizItem, responseTime: Int, alternative: Alternative) {

        if (alternative.isCorrect) {
            QuizResultHandler.addCorrectAnswer(responseTime)
        }

        val request = QuizRequest(
            quizId = quizItem.id,
            responseTime = responseTime,
            alternative = alternative
        )

        viewModelScope.launch {
            postQuizUseCase(PostQuizUseCase.Params(request)) {
                Logger.withTag(QuizViewModel::class.java.simpleName).i("Post quiz returned: $it")
            }
        }
    }

    companion object {
        const val DEFAULT_CATEGORY_FALLBACK = 1
    }

}