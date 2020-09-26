package com.sabidos.presentation.components

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.sabidos.R

class AppLoadingComponent {

    private var dialog: Dialog? = null

    fun build(context: Context?) {

        context?.let {
            dialog = Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.sabidos_custom_loading_dialog)

                window?.attributes = WindowManager.LayoutParams().apply {
                    copyFrom(window?.attributes)
                    width = WindowManager.LayoutParams.WRAP_CONTENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                    gravity = Gravity.CENTER
                }

            }
        }

    }

    fun show() {
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}