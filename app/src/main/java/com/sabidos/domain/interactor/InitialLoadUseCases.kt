package com.sabidos.domain.interactor

import com.sabidos.domain.Account
import com.sabidos.domain.repository.InitialLoadRepository
import com.sabidos.infrastructure.ResultWrapper
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoadInitialDataUseCase(private val initialLoadRepository: InitialLoadRepository) :
    UseCase<Account?, None>() {

    override suspend fun run(params: None): ResultWrapper<Account?> = suspendCoroutine { cont ->
        initialLoadRepository.load {
            cont.resume(it)
        }
    }

}