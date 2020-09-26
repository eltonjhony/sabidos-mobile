package com.sabidos.presentation.components

import android.content.Context
import android.util.AttributeSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import kotlinx.android.synthetic.main.sabidos_checkbox_selection_item.view.*

class CheckboxSelectionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    layoutId = R.layout.sabidos_checkbox_selection_item,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    var isFirstOption: Boolean = false
        set(value) {
            field = value
            if (!value) {
                topDelimiterView.hide()
            }
        }

    var optionLabel: String = ""
        set(value) {
            field = value
            optionText.text = value
        }
    
    override fun setupComponent() {
        super.setupComponent()
        checkSelectSymbolView.hide()
    }

    fun showCheckSymbol() {
        checkSelectSymbolView.show()
    }

    fun hideCheckSymbol() {
        checkSelectSymbolView.hide()
    }

}