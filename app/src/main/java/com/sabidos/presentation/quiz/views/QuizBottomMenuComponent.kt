package com.sabidos.presentation.quiz.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import com.sabidos.R
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.presentation.MainActivity
import com.sabidos.presentation.quiz.SabidosQuizActivity
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_quiz_bottom_menu_component.view.*

class QuizBottomMenuComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_quiz_bottom_menu_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    fun setup(context: AppCompatActivity) {
        hide()
        closeLinkView.setOnClickListener {
            context.goTo(MainActivity::class.java)
        }

        nextButton.setOnClickListener {
            context.goTo(SabidosQuizActivity::class.java)
        }

    }

}