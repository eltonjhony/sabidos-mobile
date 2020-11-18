package com.sabidos.infrastructure.extensions

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnRepeat
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sabidos.R
import com.sabidos.infrastructure.logging.Logger

fun ImageView.load(url: String?) {
    runCatching {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.placeholderOf(R.mipmap.placeholder))
            .into(this)
    }.onFailure {
        Logger.withTag(View::class.java.simpleName).withCause(it)
        this.setImageDrawable(context.drawable(R.mipmap.placeholder))
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun NestedScrollView.focusOnView(view: View, delay: Long = 500) {
    runCatching {
        postDelayed({
            smoothScrollTo(
                0,
                (view.y / 2).toInt()
            )
        }, delay)
    }.onFailure {
        Logger.withTag(View::class.java.simpleName).withCause(it)
    }
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.hideWithAnimation() {
    runCatching {
        animate()
            .translationY(height.toFloat())
            .alpha(1.0f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    hide()
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            })
    }.onFailure {
        hide()
        Logger.withTag(View::class.java.simpleName).withCause(it)
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.setLayoutParams(
    width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) {
    layoutParams = ViewGroup.LayoutParams(
        width,
        height
    )
}

fun View.setConstraintLayoutParams(
    width: Int = ConstraintLayout.LayoutParams.MATCH_PARENT,
    height: Int = ConstraintLayout.LayoutParams.WRAP_CONTENT
) {
    val params = layoutParams
    params.width = width
    params.height = height
    layoutParams = params
}

fun View.margin(
    left: Float? = null,
    top: Float? = null,
    right: Float? = null,
    bottom: Float? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.MarginLayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun ConstraintLayout.addConstraints(define: (ConstraintSet) -> Unit) {
    val constraints = ConstraintSet()
    constraints.clone(this)
    define(constraints)
    constraints.applyTo(this)
}

fun TextView.applyTextAppearance(styleId: Int) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        setTextAppearance(context, styleId)
    } else {
        setTextAppearance(styleId)
    }

fun View.pump(duration: Long = 5000, pumpDuration: Long = 1000, valueAnimator: Float = 1.08f) {
    runCatching {
        val animator = ValueAnimator.ofFloat(valueAnimator)
        animator.doOnRepeat {
            animate()
                .scaleX(animator.animatedValue as Float)
                .scaleY(animator.animatedValue as Float)
                .translationZ(context.dpToPx(5f).toFloat())
                .withEndAction {
                    animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .translationZ(0f)
                }
                .start()
        }
        animator.duration = pumpDuration
        animator.interpolator = DecelerateInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            animator.removeAllListeners()
            animator.end()
            animator.cancel()
        }, duration)
    }.onFailure {
        Logger.withTag(View::class.java.simpleName).withCause(it)
    }

}

fun EditText.onChange(callback: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            callback(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            /*abstract implementation*/
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            /*abstract implementation*/
        }
    })
}

fun TextView.clickableText(longTex: String, clickableText: String, onClickCallback: (() -> Unit)) {
    stylableText(longTex, clickableText, true, onClickCallback) { ds ->
        context?.let {
            ds.isUnderlineText = true
            ds.textSize = it.dpToPx(16f).toFloat()
            ds.typeface = Typeface.create(ds.typeface, Typeface.BOLD)
            ds.color = it.color(R.color.blueLinkColor)
        }
    }
}

fun TextView.stylableText(
    longTex: String,
    stylableText: String,
    isLink: Boolean = false,
    onClickCallback: (() -> Unit)? = null,
    onStylableCallback: ((ds: TextPaint) -> Unit)
) {

    val spannable = SpannableString("$longTex $stylableText")
    val clickableSpan: ClickableSpan = object : ClickableSpan() {

        override fun onClick(view: View) {
            onClickCallback?.let { it() }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            onStylableCallback(ds)
        }
    }

    spannable.setSpan(
        clickableSpan,
        longTex.length + 1,
        (longTex.length + stylableText.length + 1),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    text = spannable

    if (isLink) {
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }
}

fun View.didSelect() {
    runCatching {
        background = context.drawable(
            R.drawable.item_selection_background
        )
        val fadeIn = ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        fadeIn.duration = 400
        fadeIn.fillAfter = true
        startAnimation(fadeIn)
    }.onFailure { Logger.withTag(View::class.java.simpleName).withCause(it) }
}