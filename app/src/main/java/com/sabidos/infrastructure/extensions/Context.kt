package com.sabidos.infrastructure.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.showKeyboard() {
    val imm: InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.drawable(@DrawableRes id: Int): Drawable? = runCatching {
    ContextCompat.getDrawable(this, id)
}.getOrNull()

fun Context.color(@ColorRes id: Int): Int =
    ContextCompat.getColor(this, id)