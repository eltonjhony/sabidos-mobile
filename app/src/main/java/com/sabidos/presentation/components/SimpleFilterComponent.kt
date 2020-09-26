package com.sabidos.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.Button
import com.sabidos.R
import com.sabidos.infrastructure.extensions.dpToPx
import com.sabidos.infrastructure.extensions.drawable
import com.sabidos.infrastructure.extensions.margin
import com.sabidos.infrastructure.extensions.setConstraintLayoutParams
import kotlinx.android.synthetic.main.sabidos_simple_filter_component.view.*

class SimpleFilterComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_simple_filter_component,
    R.styleable.SimpleFilterComponent,
    context,
    attrs,
    defStyleAttr
) {

    var firstOption: FilterModel? = null
        set(value) {
            field = value
            configure(value, firstSelectionOption)
            firstSelectionOption.setOnClickListener {
                disable(secondSelectionOption)
                active(firstSelectionOption)
                value?.filterCallback?.invoke()
            }
        }

    var secondOption: FilterModel? = null
        set(value) {
            field = value
            configure(value, secondSelectionOption)
            secondSelectionOption.setOnClickListener {
                disable(firstSelectionOption)
                active(secondSelectionOption)
                value?.filterCallback?.invoke()
            }
        }

    override fun setCustomAttributes(attributeSet: TypedArray) {
        super.setCustomAttributes(attributeSet)
        firstSelectionOption.setConstraintLayoutParams(
            width = context.dpToPx(
                attributeSet.getFloat(
                    R.styleable.SimpleFilterComponent_filterWidth,
                    90f
                )
            ),
            height = context.dpToPx(
                attributeSet.getFloat(
                    R.styleable.SimpleFilterComponent_filterHeight,
                    25f
                )
            )
        )
        secondSelectionOption.setConstraintLayoutParams(
            width = context.dpToPx(
                attributeSet.getFloat(
                    R.styleable.SimpleFilterComponent_filterWidth,
                    90f
                )
            ),
            height = context.dpToPx(
                attributeSet.getFloat(
                    R.styleable.SimpleFilterComponent_filterHeight,
                    25f
                )
            )
        )
        secondSelectionOption.margin(
            left = attributeSet.getFloat(
                R.styleable.SimpleFilterComponent_filterMarginStart,
                70f
            )
        )
    }

    private fun active(option: Button) {
        option.background =
            context.drawable(R.drawable.filter_background_active_rounded)
        option.bringToFront()
    }

    private fun disable(option: Button) {
        option.background =
            context.drawable(R.drawable.filter_background_disabled_rounded)
    }

    private fun configure(model: FilterModel?, option: Button) {

        model?.let {
            option.text = it.text

            if (it.isDefault) {
                option.background =
                    context.drawable(R.drawable.filter_background_active_rounded)
            } else {
                option.background =
                    context.drawable(
                        R.drawable.filter_background_disabled_rounded
                    )
            }
        }

    }

    data class FilterModel(
        val text: String,
        val isDefault: Boolean = false,
        val filterCallback: () -> Unit
    )

}