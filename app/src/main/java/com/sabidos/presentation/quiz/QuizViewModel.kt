package com.sabidos.presentation.quiz

import androidx.lifecycle.MutableLiveData
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Alternative
import com.sabidos.domain.Quiz
import com.sabidos.domain.interactor.GetCurrentAccountUseCase
import com.sabidos.domain.interactor.GetNextQuizUseCase
import com.sabidos.domain.interactor.GetNextQuizUseCase.Params
import com.sabidos.domain.interactor.PostQuizUseCase
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.loading
import com.sabidos.infrastructure.extensions.setGenericFailure
import com.sabidos.infrastructure.extensions.setNetworkFailure
import com.sabidos.infrastructure.extensions.setSuccess
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.common.AccountViewModel

class QuizViewModel(
    getCurrentAccountUseCase: GetCurrentAccountUseCase,
    private val getNextQuizUseCase: GetNextQuizUseCase,
    private val postQuizUseCase: PostQuizUseCase
) :
    AccountViewModel(getCurrentAccountUseCase) {

    val quizResource = MutableLiveData<Resource<Quiz>>()

    fun getNextQuizFor(categoryId: Int) {
        quizResource.loading()
        getNextQuizUseCase(Params(categoryId)) {
            when (it) {
                is ResultWrapper.Success -> quizResource.setSuccess(it.data)
                is ResultWrapper.NetworkError -> quizResource.setNetworkFailure()
                else -> quizResource.setGenericFailure()
            }
        }

    }

    fun postQuiz(quiz: Quiz, timeToAnswer: Int, alternative: Alternative) {

        val request = QuizRequest(
            quizId = quiz.id,
            timeToAnswer = timeToAnswer,
            alternative = alternative
        )

        postQuizUseCase(PostQuizUseCase.Params(request)) {
            Logger.withTag(QuizViewModel::class.java.simpleName).i("Post quiz returned: $it")
        }
    }

}