package com.sabidos.presentation.quiz.views

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
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

    var onNextCallback: (() -> Unit)? = null
        set(value) {
            field = value
            setup()
        }

    private fun setup() {
        hide()
        nextButton.setOnClickListener {
            onNextCallback?.invoke()
        }
    }

}