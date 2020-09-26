package com.sabidos.domain.interactor

import com.sabidos.domain.Account
import com.sabidos.domain.Timeline
import com.sabidos.domain.User
import com.sabidos.domain.WeeklyHits
import com.sabidos.domain.repository.AccountRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.domain.interactor.CreateAccountUseCase.Params as SaveAccountParam
import com.sabidos.domain.interactor.GetTimelineUseCase.Params as GetTimelineParam
import com.sabidos.domain.interactor.GetWeeklyHitsUseCase.Params as GetWeeklyHitsParam

class CreateAccountUseCase(private val accountRepository: AccountRepository) : UseCase<Account, SaveAccountParam>() {

    override suspend fun run(params: SaveAccountParam): ResultWrapper<Account> =
        accountRepository.createAccount(params.account, params.user)

    data class Params(val account: Account, val user: User)
}

class CreateAnonymousAccountUseCase(private val accountRepository: AccountRepository) : UseCase<Account, None>() {

    override suspend fun run(params: None): ResultWrapper<Account> =
        accountRepository.createAnonymousAccount()
}

class GetCurrentAccountUseCase(private val accountRepository: AccountRepository) : UseCase<Account?, None>() {

    override suspend fun run(params: None): ResultWrapper<Account?> {
        return accountRepository.getCurrentAccount()
    }
}

class GetWeeklyHitsUseCase(private val accountRepository: AccountRepository) :
    UseCase<List<WeeklyHits>, GetWeeklyHitsParam>() {

    override suspend fun run(params: GetWeeklyHitsParam): ResultWrapper<List<WeeklyHits>> =
        accountRepository.getWeeklyHits(params.endDate)

    data class Params(val endDate: String)
}

class GetTimelineUseCase(private val accountRepository: AccountRepository) :
    UseCase<List<Timeline>, GetTimelineParam>() {

    override suspend fun run(params: GetTimelineParam): ResultWrapper<List<Timeline>> =
        accountRepository.getTimeline(params.page)

    data class Params(val page: Int)
}
