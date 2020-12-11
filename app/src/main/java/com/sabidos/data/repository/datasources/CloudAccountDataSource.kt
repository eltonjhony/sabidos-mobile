package com.sabidos.data.repository.datasources

import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.mapper.*
import com.sabidos.data.remote.AccountService
import com.sabidos.data.remote.CloudApiFactory
import com.sabidos.data.remote.ErrorHandling
import com.sabidos.data.remote.NetworkHandler
import com.sabidos.data.remote.model.AccountRequest
import com.sabidos.domain.Account
import com.sabidos.domain.Timeline
import com.sabidos.domain.User
import com.sabidos.domain.WeeklyHits
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.oauth.OAuthProvider

class CloudAccountDataSource(
    private val cloudApiFactory: CloudApiFactory,
    private val oAuthProvider: OAuthProvider,
    private val accountCache: AccountCache,
    private val networkHandler: NetworkHandler
) {

    suspend fun createAccount(account: Account, user: User): ResultWrapper<Account> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val bodyRequest = AccountToRequestMapper.transform(account)
                    bodyRequest.apply {
                        uid = user.uid
                        isAnonymous = user.isAnonymous
                        email = user.email
                        phone = user.phoneNumber
                    }

                    val service = cloudApiFactory.create(AccountService::class.java)
                    val updatedAccount =
                        ResponseToAccountMapper.transform(service.createAccount(bodyRequest))
                    accountCache.put(AccountToEntityMapper.transform(updatedAccount))
                    ResultWrapper.Success(updatedAccount)

                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun validateAccount(nickname: String): ResultWrapper<Boolean> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val bodyRequest = AccountRequest(nickname = nickname)
                    val service = cloudApiFactory.create(AccountService::class.java)
                    service.validateAccount(bodyRequest)
                    ResultWrapper.Success(true)
                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun createAnonymousAccount(): ResultWrapper<Account> {
        return when {
            networkHandler.isConnected -> {
                runCatching {

                    when (val userResult = oAuthProvider.getCurrentUser()) {
                        is ResultWrapper.Success -> {

                            val bodyRequest = hashMapOf("uid" to userResult.data.uid)
                            val service = cloudApiFactory.create(AccountService::class.java)
                            val updatedAccount =
                                ResponseToAccountMapper.transform(
                                    service.createAnonymousAccount(
                                        bodyRequest
                                    )
                                )

                            accountCache.put(AccountToEntityMapper.transform(updatedAccount))
                            ResultWrapper.Success(updatedAccount)
                        }
                        else -> ResultWrapper.GenericError()
                    }

                }.getOrElse {
                    ErrorHandling.parse(it)
                }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun getAccountBy(nickname: String): ResultWrapper<Account> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(AccountService::class.java)

                    val result = service.getAccountBy(nickname)

                    result.account?.let {
                        val account = ResponseToAccountMapper.transform(it)
                        accountCache.put(AccountToEntityMapper.transform(account))
                        ResultWrapper.Success(account)
                    } ?: ResultWrapper.DataNotFoundError

                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun getAccountByUserUid(): ResultWrapper<Account> {
        return when {
            networkHandler.isConnected -> {
                runCatching {

                    when (val userResult = oAuthProvider.getCurrentUser()) {
                        is ResultWrapper.Success -> {

                            val service = cloudApiFactory.create(AccountService::class.java)
                            val result = service.getAccountByUid(userResult.data.uid)

                            result.account?.let {
                                val account = ResponseToAccountMapper.transform(it)
                                accountCache.put(AccountToEntityMapper.transform(account))
                                ResultWrapper.Success(account)
                            } ?: ResultWrapper.DataNotFoundError

                        }
                        else -> ResultWrapper.GenericError()
                    }

                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun getWeeklyHitsBy(
        nickname: String,
        endDate: String
    ): ResultWrapper<List<WeeklyHits>> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(AccountService::class.java)

                    val result = service.getWeeklyHits(nickname, endDate)

                    if (result.hits.isEmpty()) {
                        ResultWrapper.Success(emptyList())
                    } else {
                        val response = ResponseToWeeklyHitsMapper.transform(result.hits)
                        ResultWrapper.Success(response)
                    }

                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun getTimelineFor(nickname: String, page: Int): ResultWrapper<List<Timeline>> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(AccountService::class.java)

                    val result = service.getTimelineFor(nickname, page)

                    if (result.questions.isEmpty()) {
                        ResultWrapper.Success(emptyList())
                    } else {
                        val response = ResponseToTimelineMapper.transform(
                            result.questions
                        )
                        ResultWrapper.Success(response)
                    }

                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

}