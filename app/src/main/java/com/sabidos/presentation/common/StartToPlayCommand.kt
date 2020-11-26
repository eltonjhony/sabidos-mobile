package com.sabidos.presentation.common

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.sabidos.domain.Category
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.quiz.SabidosQuizActivity

object StartToPlayCommand {

    fun playWithCategory(activity: FragmentActivity?, category: Category) {
        runCatching {
            val bundle = Bundle()
            bundle.putInt(SabidosQuizActivity.CATEGORY_ID_BUNDLE_KEY, category.id)
            activity?.goTo(SabidosQuizActivity::class.java, false, bundle = bundle)
        }.onFailure {
            Logger.withTag(StartToPlayCommand::class.java.simpleName).withCause(it)
        }
    }

}