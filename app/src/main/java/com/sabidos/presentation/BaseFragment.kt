package com.sabidos.presentation

import android.text.Spanned
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sabidos.data.remote.model.ErrorResponse
import com.sabidos.infrastructure.extensions.*
import com.sabidos.presentation.components.AppLoadingComponent

open class BaseFragment : Fragment() {
    val loading: AppLoadingComponent by lazy { AppLoadingComponent() }

    fun showNetworkErrorDialog() =
        activity?.let { (it as AppCompatActivity).showNetworkErrorDialog() }

    fun showApiError(error: ErrorResponse?) =
        activity?.let { (it as AppCompatActivity).showApiError(error) }

    fun showGenericErrorDialog() =
        activity?.let { (it as AppCompatActivity).showGenericErrorDialog() }

    fun showDialog(
        @DrawableRes icon: Int,
        title: String,
        message: String,
        @ColorRes iconBackgroundColor: Int? = null
    ) =
        activity?.let {
            (it as AppCompatActivity).showDialog(
                icon,
                title,
                message,
                iconBackgroundColor
            )
        }

    fun showAnimationDialog(
        jsonAnimation: String,
        title: String,
        message: String? = null,
        spanned: Spanned? = null
    ) =
        activity?.let {
            (it as AppCompatActivity).showAnimationDialog(
                jsonAnimation,
                title,
                message,
                spanned = spanned
            )
        }

}