package com.sabidos.infrastructure.di

import android.content.Context
import com.sabidos.data.local.*
import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.local.cache.AvatarCache
import com.sabidos.data.local.cache.CacheHandler
import com.sabidos.data.local.cache.CategoryCache
import com.sabidos.data.local.preferences.RoundPrefsHelper
import com.sabidos.data.remote.CloudApiFactory
import com.sabidos.data.remote.NetworkHandler
import com.sabidos.data.remote.SecurityCloudApiFactory
import com.sabidos.data.repository.*
import com.sabidos.data.repository.datasources.*
import com.sabidos.domain.interactor.*
import com.sabidos.domain.repository.*
import com.sabidos.infrastructure.helpers.PhoneNumberHelper
import com.sabidos.infrastructure.helpers.SignInPrefsHelper
import com.sabidos.infrastructure.oauth.OAuthProvider
import com.sabidos.infrastructure.oauth.providers.FirebaseOAuthProvider
import com.sabidos.presentation.MainViewModel
import com.sabidos.presentation.category.CategoryViewModel
import com.sabidos.presentation.home.HomeViewModel
import com.sabidos.presentation.onboarding.LoginViewModel
import com.sabidos.presentation.onboarding.OnboardingViewModel
import com.sabidos.presentation.onboarding.PhoneVerificationViewModel
import com.sabidos.presentation.onboarding.UserAvatarViewModel
import com.sabidos.presentation.profile.AccountManagementViewModel
import com.sabidos.presentation.profile.MyPerformanceViewModel
import com.sabidos.presentation.profile.ProfileViewModel
import com.sabidos.presentation.quiz.QuizViewModel
import com.sabidos.presentation.quiz.ResultsViewModel
import com.sabidos.presentation.ranking.RankingViewModel
import com.sabidos.presentation.splash.DeepLinksViewModel
import com.sabidos.presentation.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
    viewModel { PhoneVerificationViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { DeepLinksViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { AccountManagementViewModel(get(), get()) }
    viewModel { MyPerformanceViewModel(get()) }
    viewModel { RankingViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { UserAvatarViewModel(get()) }
    viewModel { QuizViewModel(get(), get()) }
    viewModel { ResultsViewModel(get()) }
}

val domainModule = module {
    single { IsUserLoggedUseCase(get()) }
    single { GetCurrentUserUseCase(get()) }
    single { SignInAnonymouslyUseCase(get()) }
    single { SignInWithEmailLinkUseCase(get()) }
    single { SignInWithPhoneNumberUseCase(get()) }
    single { VerifyPhoneNumberUseCase(get()) }
    single { CompleteSignInWithEmailLinkUseCase(get()) }
    single { IsSignInWithEmailLinkUseCase(get()) }
    single { SignOutUseCase(get(), get(), get()) }
    single { CreateAccountUseCase(get()) }
    single { CreateAnonymousAccountUseCase(get()) }
    single { GetCurrentAccountUseCase(get()) }
    single { GetWeeklyHitsUseCase(get()) }
    single { GetTimelineUseCase(get()) }
    single { GetWeeklyRankingUseCase(get()) }
    single { GetAllCategoriesUseCase(get()) }
    single { LoadInitialDataUseCase(get()) }
    single { GetAllAvatarsUseCase(get()) }
    single { GetNextRoundUseCase(get()) }
    single { PostQuizUseCase(get()) }
    single { SyncQuizUseCase(get()) }
    single { PostRoundUseCase(get()) }
}

val infrastructureModule = module {
    single { SignInPrefsHelper(androidContext()) }
    single { PhoneNumberHelper() }
    single<OAuthProvider> { FirebaseOAuthProvider(get()) }
}

val dataModule = module {

    single { RoundPrefsHelper(androidContext()) }

    single { CacheHandler(get(), get(), get()) }

    single {
        AccountCache(
            get(),
            androidContext().getSharedPreferences(
                "com.sabidos.caching",
                Context.MODE_PRIVATE
            )
        )
    }

    single {
        CategoryCache(
            get(),
            androidContext().getSharedPreferences(
                "com.sabidos.caching",
                Context.MODE_PRIVATE
            )
        )
    }

    single {
        AvatarCache(
            get(),
            androidContext().getSharedPreferences(
                "com.sabidos.caching",
                Context.MODE_PRIVATE
            )
        )
    }

    single { AppDatabase.getDatabase(androidContext()) }
    single { AppDatabase.getDatabase(androidContext()).accountDao() } bind AccountDao::class
    single { AppDatabase.getDatabase(androidContext()).categoryDao() } bind CategoryDao::class
    single { AppDatabase.getDatabase(androidContext()).avatarDao() } bind AvatarDao::class
    single { AppDatabase.getDatabase(androidContext()).quizDao() } bind QuizDao::class

    single { LocalAccountDataSource(get()) }
    single { LocalCategoryDataSource(get()) }
    single { LocalAvatarDataSource(get()) }
    single { LocalQuizDataSource(get()) }

    single { CloudAccountDataSource(get(), get(), get(), get()) }
    single { CloudRankingDataSource(get(), get()) }
    single { CloudCategoryDataSource(get(), get(), get()) }
    single { CloudAvatarDataSource(get(), get(), get()) }

    single { CloudQuizDataSource(get(), get()) }

    single<AccountRepository> { AccountDataRepository(get(), get(), get()) }
    single<RankingRepository> { RankingDataRepository(get(), get()) }
    single<CategoryRepository> { CategoryDataRepository(get(), get(), get()) }
    single<AvatarRepository> { AvatarDataRepository(get(), get(), get()) }
    single<InitialLoadRepository> { InitialLoadDataRepository(get(), get(), get()) }
    single<QuizRepository> { QuizDataRepository(get(), get(), get(), get()) }

}

val cloudModule = module {
    single { NetworkHandler(androidContext()) }
    single<CloudApiFactory> { SecurityCloudApiFactory(get()) }
}

val appModules =
    listOf(presentationModule, domainModule, infrastructureModule, dataModule, cloudModule)