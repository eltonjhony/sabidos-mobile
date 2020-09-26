package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintSet
import com.sabidos.R
import com.sabidos.infrastructure.extensions.addConstraints
import com.sabidos.infrastructure.extensions.margin
import com.sabidos.infrastructure.extensions.setLayoutParams
import kotlinx.android.synthetic.main.sabidos_checkbox_selection_component.view.*

class CheckboxSelectionComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_checkbox_selection_component,
    R.styleable.CheckboxSelectionComponent,
    context,
    attrs,
    defStyleAttr
) {

    var selectedOption: CheckSelectionModel? = null

    var options: List<CheckSelectionModel> = emptyList()
        set(value) {
            field = value
            buildOptions()
        }

    var didSelect: (() -> Unit?)? = null

    override fun setCustomAttributes(attributeSet: TypedArray) {
        super.setCustomAttributes(attributeSet)
        checkGroupTitle.text =
            attributeSet.getString(R.styleable.CheckboxSelectionComponent_checkGroupText)
    }

    private fun buildOptions() {

        options.forEachIndexed { index, option ->

            val viewOption = CheckboxSelectionItem(context)
            viewOption.id = option.id
            viewOption.setLayoutParams()
            viewOption.isFirstOption = index == 0
            viewOption.optionLabel = option.label
            viewOption.setOnClickListener {
                options.forEach { findViewById<CheckboxSelectionItem>(it.id).hideCheckSymbol() }
                viewOption.showCheckSymbol()
                selectedOption = option
                didSelect?.invoke()
            }

            parentLayout.addView(viewOption)

            when (index) {
                0 -> configureFirstElement(viewOption)
                else -> configureOtherElements(viewOption, options[index - 1].id)
            }

        }

    }

    private fun configureFirstElement(viewOption: CheckboxSelectionItem) {
        configureOtherElements(viewOption, checkGroupTitle.id)
        viewOption.margin(top = 14f)
    }

    private fun configureOtherElements(
        viewOption: CheckboxSelectionItem,
        topElementId: Int
    ) {
        parentLayout.addConstraints {
            it.connect(
                viewOption.id,
                ConstraintSet.TOP,
                topElementId,
                ConstraintSet.BOTTOM,
                0
            )
        }
    }

    data class CheckSelectionModel(val id: Int, val label: String)
}