package com.sabidos.domain.repository

import com.sabidos.domain.Account
import com.sabidos.infrastructure.ResultWrapper

interface InitialLoadRepository {
    fun load(callback: ((ResultWrapper<Account>) -> Unit))
}