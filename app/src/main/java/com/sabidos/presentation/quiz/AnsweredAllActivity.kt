package com.sabidos.presentation.quiz

import android.os.Bundle
import com.sabidos.R
import com.sabidos.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_answered_all.*

class AnsweredAllActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answered_all)
        nextButton.setOnClickListener {
            super.onBackPressed()
        }
    }

}