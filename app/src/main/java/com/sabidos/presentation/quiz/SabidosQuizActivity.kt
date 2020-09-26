package com.sabidos.presentation.quiz

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.domain.Quiz
import com.sabidos.infrastructure.Constants
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.*
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_sabidos_quiz.*
import kotlinx.android.synthetic.main.content_quiz.*
import org.koin.android.viewmodel.ext.android.viewModel

class SabidosQuizActivity : BaseActivity() {

    private var categoryId: Int? = null

    private val viewModel: QuizViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        setContentView(R.layout.activity_sabidos_quiz)
        loading.build(this)
        viewModel.getCurrentAccount()
        viewModel.accountResource.observe(this, Observer { bindAccountState(it) })

        categoryId = intent.extras?.get(CATEGORY_ID_BUNDLE_KEY) as? Int
        viewModel.getNextQuizFor(categoryId ?: 1)
        viewModel.quizResource.observe(this, Observer { bindQuizState(it) })
    }

    override fun onBackPressed() {
        if (shouldAllowBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        quizProgressTimerComponent.stop()
    }

    private fun bindQuizState(resource: Resource<Quiz>?) = resource?.let {
        if (it.state != Loading) {
            loading.dismiss()
        }
        when (it.state) {
            Loading -> {
                overlayView.show()
                loading.show()
            }
            Success -> {
                overlayView.hide()
                handleQuizSuccessResult(it.data)
            }
            NetworkError -> {
                overlayView.showNetworkErrorWithRetry {
                    viewModel.getNextQuizFor(categoryId ?: 1)
                }
            }
            else -> overlayView.showErrorWithRetry {
                viewModel.getNextQuizFor(categoryId ?: 1)
            }
        }
    }

    private fun handleQuizSuccessResult(data: Quiz?) {
        data?.let {
            configureBasicViewElements(it)
            configureSelectionComponent(it)
            configureQuizProgressTimer()
        }
    }

    private fun bindAccountState(resource: Resource<Account?>?) = resource?.let {
        if (it.state is Success) {
            it.data?.let { account ->
                levelStatusComponent.show()
                starsLevelComponent.show()
                levelStatusComponent.level = account.reputation?.level
                starsLevelComponent.stars = account.reputation?.stars
            }
        }
    }

    private fun configureQuizProgressTimer() {
        quizProgressTimerComponent.onTimerFinishedCallback = {
            quizSelectionComponent.isTimerOver = true
            showAnimationDialog(
                Constants.Animation.TIME_OVER_ANIMATION_JSON,
                title = getString(R.string.time_over_title),
                message = getString(
                    R.string.time_over_message
                ),
                closeLabel = getString(R.string.try_again_label),
                onCloseCallback = {
                    goTo(SabidosQuizActivity::class.java)
                }
            )
        }

        quizProgressTimerComponent.start()
    }

    private fun configureBasicViewElements(quiz: Quiz) {
        quiz.imageUrl?.let {
            quizTopImageView.show()
            quizTopImageView.load(it)
        }
        questionDescTextView.text = quiz.description
        quizExplanationComponent.configureView(quiz.explanation, quiz.getCorrectAnswer())
        quizBottomMenuComponent.setup(this)
    }

    private fun configureSelectionComponent(quiz: Quiz) {
        quizSelectionComponent.alternatives = quiz.alternatives
        quizSelectionComponent.onDidClickCallback = { alternative ->
            quizProgressTimerComponent.stop()
            quizAnswerResultComponent.showResultFor(alternative.isCorrect)
            viewModel.postQuiz(quiz, quizProgressTimerComponent.timeToAnswer, alternative)
        }
        quizSelectionComponent.onSelectionCallback = {
            quizExplanationComponent.show()
            quizBottomMenuComponent.show()
            applyContentBottomPadding()
            quizNestedScrollView.focusOnView(quizExplanationComponent)
        }
    }

    private fun applyContentBottomPadding() {
        quizBottomMenuComponent.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                runCatching {
                    quizBottomMenuComponent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    contentWrapperLayout.setPadding(
                        0, 0, 0,
                        quizBottomMenuComponent.measuredHeight
                    )
                }.onFailure {
                    Logger.withTag(SabidosQuizActivity::class.java.simpleName).withCause(it)
                }
            }
        })
    }

    private fun shouldAllowBackPressed(): Boolean = runCatching {
        quizSelectionComponent.isTimerOver || quizExplanationComponent.isVisible
    }.getOrElse {
        Logger.withTag(SabidosQuizActivity::class.java.simpleName).withCause(it)
        false
    }

    companion object {
        const val CATEGORY_ID_BUNDLE_KEY = "CATEGORY_ID_BUNDLE"
    }

}