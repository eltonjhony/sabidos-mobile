package com.sabidos.data.repository

import com.sabidos.domain.Account
import com.sabidos.domain.repository.AccountRepository
import com.sabidos.domain.repository.AvatarRepository
import com.sabidos.domain.repository.CategoryRepository
import com.sabidos.domain.repository.InitialLoadRepository
import com.sabidos.infrastructure.ResultWrapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class InitialLoadDataRepository(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val avatarRepository: AvatarRepository
) : InitialLoadRepository {

    override fun load(callback: ((ResultWrapper<Account>) -> Unit)) {

        GlobalScope.launch {
            supervisorScope {

                val fetchCurrentAccount = async { accountRepository.getCurrentAccount() }
                val fetchCategories = async { categoryRepository.getAllCategories() }
                val fetchAvatars = async { avatarRepository.getAllAvatars() }

                val accountResult = runCatching {
                    fetchCurrentAccount.await()
                }.getOrElse { ResultWrapper.GenericError(Error(it.message)) }

                runCatching {
                    fetchCategories.await()
                }.getOrElse { ResultWrapper.GenericError(Error(it.message)) }

                runCatching {
                    fetchAvatars.await()
                }.getOrElse { ResultWrapper.GenericError(Error(it.message)) }

                callback(accountResult)
            }

        }

    }

}