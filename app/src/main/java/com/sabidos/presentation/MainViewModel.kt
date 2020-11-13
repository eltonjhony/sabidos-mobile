package com.sabidos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabidos.domain.interactor.None
import com.sabidos.domain.interactor.SyncQuizUseCase
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.launch

class MainViewModel(private val syncQuizUseCase: SyncQuizUseCase) : ViewModel() {

    fun syncQuiz() {
        viewModelScope.launch {
            syncQuizUseCase(None()) {
                Logger.withTag(MainViewModel::class.java.simpleName).i("Sync quiz returned: $it")
            }
        }
    }

}