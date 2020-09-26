package com.sabidos.presentation

import androidx.lifecycle.ViewModel
import com.sabidos.domain.interactor.None
import com.sabidos.domain.interactor.SyncQuizUseCase
import com.sabidos.infrastructure.logging.Logger

class MainViewModel(private val syncQuizUseCase: SyncQuizUseCase) : ViewModel() {

    fun syncQuiz() {
        syncQuizUseCase(None()) {
            Logger.withTag(MainViewModel::class.java.simpleName).i("Sync quiz returned: $it")
        }
    }

}