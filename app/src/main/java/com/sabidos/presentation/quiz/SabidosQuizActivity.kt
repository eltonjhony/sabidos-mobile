package com.sabidos.presentation.quiz

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.data.local.preferences.RoundPrefsHelper
import com.sabidos.domain.Quiz
import com.sabidos.domain.QuizItem
import com.sabidos.infrastructure.Constants
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.*
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_sabidos_quiz.*
import kotlinx.android.synthetic.main.content_quiz.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SabidosQuizActivity : BaseActivity() {

    private var categoryId: Int? = null

    private val viewModel: QuizViewModel by viewModel()
    private val roundPrefsHelper: RoundPrefsHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sabidos_quiz)
        setupBackgroundImage()

        categoryId = intent.extras?.get(CATEGORY_ID_BUNDLE_KEY) as? Int
        fetchNewRound()

        viewModel.roundResource.observe(this, Observer { bindRoundState(it) })
        viewModel.quizItemResource.observe(this, Observer { bindQuizItemState(it) })
        viewModel.currentQuizResource.observe(this, Observer { bindRoundProgressState(it) })
    }

    override fun onDestroy() {
        super.onDestroy()
        quizTopContentComponent.stopTimer()
    }

    private fun fetchNewRound() {
        viewModel.getNewRoundFor(categoryId ?: DEFAULT_CATEGORY_FALLBACK)
    }

    private fun bindRoundState(resource: Resource<Quiz>?) = resource?.let {
        when (it.state) {
            Loading -> overlayView.startPreparingRound(roundPrefsHelper.getRound())
            Success -> {
                overlayView.didPrepareRound {
                    overlayView.hideWithAnimation()
                    viewModel.getNextQuizForRound()
                }
            }
            Finished -> {
                goTo(QuizResultActivity::class.java)
            }
            NetworkError -> {
                overlayView.showNetworkErrorWithRetry { fetchNewRound() }
            }
            else -> overlayView.showErrorWithRetry { fetchNewRound() }
        }
    }

    private fun bindQuizItemState(resource: Resource<QuizItem>?) = resource?.let {
        when (it.state) {
            Success -> {
                it.data?.let { quizItem ->
                    quizNestedScrollView.focusOnView(quizTopContentComponent, delay = 50)
                    configureBasicViewElements(quizItem)
                    configureSelectionComponent(quizItem)
                    configureQuizProgressTimer(quizItem.quizLimitInSeconds)
                }
            }
            else -> overlayView.showErrorWithRetry {
                viewModel.getNewRoundFor(categoryId ?: 1)
            }
        }
    }

    private fun bindRoundProgressState(resource: Resource<Pair<Int, Int>>?) = resource?.let {
        if (it.state is Success) {
            it.data?.let { roundProgress ->
                quizTopContentComponent.setupRoundProgress("${roundProgress.first}/${roundProgress.second}")
            }
        }
    }

    private fun configureBasicViewElements(quizItem: QuizItem) {
        quizQuestionAreaComponent.setup(quizItem)
        quizExplanationComponent.hide()
        quizItem.explanation?.let {
            quizExplanationComponent.configureView(it, quizItem.getCorrectAnswer())
        }
        quizBottomMenuComponent.onNextCallback = {
            viewModel.getNextQuizForRound()
        }
        quizAnswerResultComponent.hide()
        quizTopContentComponent.didBackPressed = {
            super.onBackPressed()
        }
    }

    private fun configureQuizProgressTimer(quizLimitInSeconds: Int) {
        quizTopContentComponent.onTimerFinishedCallback = {
            quizSelectionComponent.isTimerOver = true
            showAnimationDialog(
                Constants.Animation.TIME_OVER_ANIMATION_JSON,
                title = getString(R.string.time_over_title),
                message = getString(
                    R.string.time_over_message
                ),
                closeLabel = getString(R.string.next_label),
                onCloseCallback = {
                    viewModel.getNextQuizForRound()
                }
            )
        }

        quizTopContentComponent.startTimer(quizLimitInSeconds)
    }

    private fun configureSelectionComponent(quizItem: QuizItem) {
        quizSelectionComponent.alternatives = quizItem.alternatives
        quizSelectionComponent.onDidClickCallback = { alternative ->
            quizTopContentComponent.stopTimer()
            quizAnswerResultComponent.showResultFor(alternative.isCorrect)
            viewModel.postQuiz(quizItem, quizTopContentComponent.getResponseTime(quizItem.quizLimitInSeconds), alternative)
        }
        quizSelectionComponent.onSelectionCallback = {
            quizBottomMenuComponent.show()
            if (quizItem.explanation != null) {
                quizExplanationComponent.show()
                applyContentBottomPadding()
                quizNestedScrollView.focusOnView(quizExplanationComponent)
            }
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

    private fun setupBackgroundImage() =
        runCatching {
            contentWrapperLayout.background = baseContext.drawable(R.mipmap.quiz_background_image)
        }.getOrElse { Logger.withTag(SabidosQuizActivity::class.java.simpleName).withCause(it) }

    companion object {
        const val CATEGORY_ID_BUNDLE_KEY = "CATEGORY_ID_BUNDLE"
        const val DEFAULT_CATEGORY_FALLBACK = 1
    }

}