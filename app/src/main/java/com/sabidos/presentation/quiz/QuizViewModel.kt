package com.sabidos.presentation.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Alternative
import com.sabidos.domain.Quiz
import com.sabidos.domain.QuizItem
import com.sabidos.domain.interactor.GetNextRoundUseCase
import com.sabidos.domain.interactor.GetNextRoundUseCase.Params
import com.sabidos.domain.interactor.PostQuizUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
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

    fun getNewRoundFor(categoryId: Int) {
        roundResource.loading()
        viewModelScope.launch {
            getNextRoundUseCase(Params(categoryId)) {
                when (it) {
                    is ResultWrapper.Success -> handleSuccessRound(it.data)
                    is ResultWrapper.NetworkError -> roundResource.setNetworkFailure()
                    else -> roundResource.setGenericFailure()
                }
            }
        }

    }

    fun getNextQuizForRound() {
        val nextQuiz = roundQuizList.first()
        roundQuizList.remove(nextQuiz)
        roundTotal?.let { currentQuizResource.setSuccess(Pair(nextQuiz.position, it)) }
        quizItemResource.setSuccess(nextQuiz)
    }

    private fun handleSuccessRound(quiz: Quiz) {
        roundQuizList.addAll(quiz.questions.sortedBy { it.position })
        roundTotal = quiz.numberOfQuestions
        roundResource.setSuccess(quiz)
    }

    fun postQuiz(quizItem: QuizItem, timeToAnswer: Int, alternative: Alternative) {

        val request = QuizRequest(
            quizId = quizItem.id,
            timeToAnswer = timeToAnswer,
            alternative = alternative
        )

        viewModelScope.launch {
            postQuizUseCase(PostQuizUseCase.Params(request)) {
                Logger.withTag(QuizViewModel::class.java.simpleName).i("Post quiz returned: $it")
            }
        }
    }

}