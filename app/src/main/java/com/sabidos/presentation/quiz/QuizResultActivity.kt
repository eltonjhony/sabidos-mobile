package com.sabidos.presentation.quiz

import android.os.Bundle
import com.sabidos.R
import com.sabidos.data.local.preferences.RoundPrefsHelper
import com.sabidos.data.local.singleton.QuizResult
import com.sabidos.data.local.singleton.QuizResultHandler
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.presentation.BaseActivity
import com.sabidos.presentation.MainActivity
import com.sabidos.presentation.common.StartToPlayCommand
import kotlinx.android.synthetic.main.activity_quiz_result.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class QuizResultActivity : BaseActivity() {

    private val viewModel: ResultsViewModel by viewModel()

    private val quizResult: QuizResult by lazy { QuizResultHandler.getResults() }
    private val roundPrefsHelper: RoundPrefsHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)
        viewModel.postRound(roundPrefsHelper.getRound(), quizResult)
        roundPrefsHelper.didFinishRound()
        resultsContentComponent.setup(quizResult)
        setupButtons()
    }

    override fun onBackPressed() {
        quizResult.categoryId?.let {
            super.onBackPressed()
        } ?: goTo(MainActivity::class.java)
    }

    private fun setupButtons() {
        resultsContentComponent.onNextButtonClicked = {
            StartToPlayCommand.playWithCategory(this, quizResult.categoryId)
        }

        goBackIconView.setOnClickListener {
            onBackPressed()
        }
    }

}