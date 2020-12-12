package com.sabidos.presentation.splash

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.infrastructure.EventObserver
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.extensions.showAuthError
import com.sabidos.infrastructure.extensions.showGenericErrorDialog
import com.sabidos.infrastructure.extensions.showNetworkErrorDialog
import com.sabidos.presentation.MainActivity
import com.sabidos.presentation.common.StartToPlayCommand
import com.sabidos.presentation.onboarding.OnboardingActivity
import com.sabidos.presentation.onboarding.OnboardingActivity.Companion.FIRST_STEP_NUMBER_PARAM
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.PERSONAL_INFO_STEP
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()
    private val deepLinksViewModel: DeepLinksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handleDeepLinks { result ->

            when (result) {
                is ResultWrapper.AuthError -> {
                    showAuthError(result.errorResponse)
                }
            }

            viewModel.setupInitialSession()
            viewModel.newUserResource.observe(this, EventObserver { bindNewUserState(it) })
            viewModel.accountResource.observe(this, Observer { bindAccountState(it) })
            viewModel.userResource.observe(this, Observer { bindUserState(it) })
        }
    }

    private fun bindNewUserState(resource: Resource<Void>?) {
        resource?.let {
            when (it.state) {
                Success -> StartToPlayCommand.playWithCategory(this, finished = true)
            }
        }
    }

    private fun bindUserState(resource: Resource<User>?) {
        resource?.let {
            stopAnimation()
            when (it.state) {
                DataNotFoundError -> goTo(OnboardingActivity::class.java)
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun bindAccountState(resource: Resource<Account>?) {
        resource?.let {
            when (it.state) {
                Loading -> startLoading()
                Success -> renderSuccess()
                DataNotFoundError -> renderAccountNotFound()
                NetworkError -> showNetworkErrorDialog()
                GenericError -> showGenericErrorDialog()
            }
        }
    }

    private fun renderSuccess() {
        progressAnimationView.stopAnimation()
        goTo(MainActivity::class.java)
    }

    private fun renderAccountNotFound() {
        progressAnimationView.stopAnimation()
        val bundle = Bundle()
        bundle.putInt(FIRST_STEP_NUMBER_PARAM, PERSONAL_INFO_STEP)
        goTo(OnboardingActivity::class.java, bundle = bundle)
    }

    private fun startLoading() {
        progressAnimationView.startAnimation(false)
    }

    private fun stopAnimation() {
        progressAnimationView.stopAnimation()
    }

    private fun handleDeepLinks(callback: (ResultWrapper<Boolean>) -> Unit) {
        val data: Uri? = intent?.data

        data?.let {
            deepLinksViewModel.handle(it, callback)
        } ?: callback.invoke(ResultWrapper.Success(true))
    }

}