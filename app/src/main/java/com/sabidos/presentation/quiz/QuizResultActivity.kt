package com.sabidos.presentation.quiz

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.sabidos.R
import com.sabidos.data.local.preferences.RoundPrefsHelper
import com.sabidos.data.local.singleton.QuizResult
import com.sabidos.data.local.singleton.QuizResultHandler
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.BaseActivity
import com.sabidos.presentation.quiz.SabidosQuizActivity.Companion.CATEGORY_ID_BUNDLE_KEY
import kotlinx.android.synthetic.main.activity_quiz_result.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class QuizResultActivity : BaseActivity() {

    private val quizResult: QuizResult by lazy { QuizResultHandler.getResults() }
    private val roundPrefsHelper: RoundPrefsHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        roundPrefsHelper.didFinishRound()
        startViewAnimation()
        setupResults()
        setupButtons()
    }

    private fun startViewAnimation() = runCatching {
        resultsContentLayout.startAnimation(
            AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_up_anim
            )
        )
    }.getOrElse { Logger.withTag(QuizResultActivity::class.java.simpleName).withCause(it) }

    private fun setupResults() {
        correctPercentageLabel.text = quizResult.getResultPercentage()
        startSeekProgress()
    }

    private fun setupButtons() {
        nextButton.text = getString(R.string.next_label).toUpperCase()
        nextButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(CATEGORY_ID_BUNDLE_KEY, quizResult.categoryId)
            goTo(SabidosQuizActivity::class.java, bundle = bundle)
        }

        goBackIconView.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun startSeekProgress() {
        circularSeekComponent.max = 100f
        circularSeekComponent.progress = 0f
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val animator = ValueAnimator.ofFloat(0f, quizResult.getResultPercentageValue())
                animator.duration = 2000L
                animator.addUpdateListener { animation ->
                    GlobalScope.launch {
                        withContext(Dispatchers.Main) {
                            circularSeekComponent.progress = animation.animatedValue as Float
                        }
                    }
                }
                withContext(Dispatchers.Main) {
                    animator.start()
                }
            }
        }
    }

}