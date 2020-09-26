package com.sabidos.infrastructure.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Spanned
import android.view.Gravity
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sabidos.R
import com.sabidos.data.remote.model.ErrorResponse
import com.sabidos.infrastructure.logging.Logger
import kotlinx.android.synthetic.main.sabidos_animation_dialog.*
import kotlinx.android.synthetic.main.sabidos_custom_dialog.*
import kotlinx.android.synthetic.main.sabidos_custom_dialog.closeButton
import kotlinx.android.synthetic.main.sabidos_custom_dialog.iconLayout
import kotlinx.android.synthetic.main.sabidos_custom_dialog.messageText
import kotlinx.android.synthetic.main.sabidos_custom_dialog.titleText

fun <T> Activity.goTo(
    destination: Class<T>,
    finished: Boolean = true,
    forResult: Boolean = false,
    bundle: Bundle? = null
) where T : Activity {
    runCatching {
        if (finished) finish()
        val intent = Intent(this.applicationContext, destination)
        bundle?.let { intent.putExtras(it) }

        if (forResult) {
            startActivityForResult(intent, 100)
        } else {
            startActivity(intent)
        }

    }.onFailure {
        Logger.withTag(Activity::class.java.simpleName).withCause(it)
    }
}

fun AppCompatActivity.replaceFragmentSafely(
    fragment: Fragment,
    @IdRes containerViewId: Int,
    tag: String = fragment::class.java.canonicalName,
    backStack: Boolean = true,
    @AnimRes enterAnimation: Int = 0,
    @AnimRes exitAnimation: Int = 0,
    @AnimRes popEnterAnimation: Int = 0,
    @AnimRes popExitAnimation: Int = 0
) {
    runCatching {

        val ft = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .replace(containerViewId, fragment, tag)

        if (backStack && ft.isAddToBackStackAllowed) {
            ft.addToBackStack(tag)
        }

        if (!supportFragmentManager.isStateSaved) {
            ft.commit()
        }

    }.onFailure {
        Logger.withTag(Activity::class.java.simpleName).withCause(it)
    }
}

fun AppCompatActivity.showGenericErrorDialog() {
    showDialog(
        R.drawable.ic_generic_error,
        getString(R.string.ops_label),
        getString(R.string.generic_error_message)
    )
}

fun AppCompatActivity.hideStatusBar() {
    runCatching {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }.onFailure {
        Logger.withTag(Activity::class.java.simpleName).withCause(it)
    }
}

fun AppCompatActivity.showGenericErrorSnackBar() {
    val snack = Snackbar.make(
        findViewById(android.R.id.content),
        getString(R.string.generic_error_message),
        Snackbar.LENGTH_LONG
    )
    snack.view.setBackgroundColor(color(R.color.colorPrimary))
    snack.show()
}

fun AppCompatActivity.showNetworkErrorDialog() {
    showDialog(
        R.drawable.ic_internet,
        getString(R.string.ops_label),
        getString(R.string.internet_connection_error)
    )
}

fun AppCompatActivity.showApiError(errorResponse: ErrorResponse?) {
    errorResponse?.let {
        showDialog(
            R.drawable.ic_generic_error, getString(R.string.ops_label),
            it.message
        )
    } ?: showGenericErrorDialog()
}

fun AppCompatActivity.showDialog(
    @DrawableRes icon: Int,
    title: String,
    message: String,
    @ColorRes iconColorBackground: Int? = null
) {

    runCatching {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.sabidos_custom_dialog)

            errorIcon.setImageDrawable(context.drawable(icon))
            titleText.text = title
            messageText.text = message

            iconColorBackground?.let {
                iconLayout.background = null
                iconLayout.setBackgroundColor(context.color(it))
            }

            window?.attributes = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = context.dpToPx(350f)
                height = WRAP_CONTENT
                gravity = Gravity.CENTER
            }

            closeButton.onClickListener {
                dismiss()
            }
        }.show()
    }.onFailure {
        Logger.withTag(Activity::class.java.simpleName).withCause(it)
    }

}

fun AppCompatActivity.showAnimationDialog(
    jsonAnimation: String,
    title: String,
    closeLabel: String? = null,
    message: String? = null,
    spanned: Spanned? = null,
    onCloseCallback: (() -> Unit)? = null
) {

    runCatching {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.sabidos_animation_dialog)

            animationView.setAnimation(jsonAnimation)
            animationView.playAnimation()
            titleText.text = title

            message?.let {
                messageText.text = it
            }

            spanned?.let {
                messageText.text = it
            }

            window?.attributes = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = context.dpToPx(350f)
                height = WRAP_CONTENT
                gravity = Gravity.CENTER
            }

            closeLabel?.let { closeButton.setText(it) }
            closeButton.onClickListener {
                dismiss()
                onCloseCallback?.let { it() }
            }

        }.show()
    }.onFailure {
        Logger.withTag(Activity::class.java.simpleName).withCause(it)
    }

}