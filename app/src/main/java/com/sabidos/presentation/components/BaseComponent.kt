package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

open class BaseComponent @JvmOverloads constructor(
    layoutId: Int,
    attrsId: IntArray? = null,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseComponentProtocol {

    init {

        View.inflate(context, layoutId, this)

        if (attrs != null && attrsId != null) {
            val attributeSet =
                context.obtainStyledAttributes(attrs, attrsId, 0, 0)
            setCustomAttributes(attributeSet)
            attributeSet.recycle()
        }

        setupComponent()
    }

}

interface BaseComponentProtocol {
    fun setupComponent() {}
    fun setCustomAttributes(attributeSet: TypedArray) {}
}