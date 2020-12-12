package com.sabidos.presentation.common

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.quiz.SabidosQuizActivity

object StartToPlayCommand {

    fun playWithCategory(activity: FragmentActivity?, categoryId: Int? = null, finished: Boolean = false) {
        runCatching {
            val bundle = Bundle()
            categoryId?.let { bundle.putInt(SabidosQuizActivity.CATEGORY_ID_BUNDLE_KEY, categoryId) }
            activity?.goTo(SabidosQuizActivity::class.java, finished, bundle = bundle)
        }.onFailure {
            Logger.withTag(StartToPlayCommand::class.java.simpleName).withCause(it)
        }
    }

}