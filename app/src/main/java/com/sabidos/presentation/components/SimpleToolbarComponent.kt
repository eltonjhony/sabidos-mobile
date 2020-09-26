package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.sabidos.R
import kotlinx.android.synthetic.main.sabidos_simple_toolbar_component.view.*

class SimpleToolbarComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.sabidos_simple_toolbar_component, this)

        if (attrs != null) {
            val attributeSet =
                context.obtainStyledAttributes(attrs, R.styleable.SimpleToolbarComponent, 0, 0)
            setCustomAttributes(attributeSet)
            attributeSet.recycle()
        }

    }

    private fun setCustomAttributes(attributeSet: TypedArray) {
        toolbarTitleLabel.text =
            attributeSet.getString(R.styleable.SimpleToolbarComponent_toolbarTitle)
    }

}