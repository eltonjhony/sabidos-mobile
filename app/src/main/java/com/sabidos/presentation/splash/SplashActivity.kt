package com.sabidos.presentation.splash

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.extensions.showGenericErrorDialog
import com.sabidos.infrastructure.extensions.showNetworkErrorDialog
import com.sabidos.infrastructure.helpers.DeepLinksHelper
import com.sabidos.presentation.MainActivity
import com.sabidos.presentation.onboarding.OnboardingActivity
import com.sabidos.presentation.onboarding.OnboardingActivity.Companion.FIRST_STEP_NUMBER_PARAM
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.PERSONAL_INFO_STEP
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    private val deepLinksHelper: DeepLinksHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        progressAnimationView.setup()

        handleDeepLinks {
            viewModel.setupInitialSession()
            viewModel.accountResource.observe(this, Observer { bindAccountState(it) })
            viewModel.userResource.observe(this, Observer { bindUserState(it) })
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
        GlobalScope.launch {
            progressAnimationView.startAnimation()
        }
    }

    private fun stopAnimation() {
        progressAnimationView.stopAnimation()
    }

    private fun handleDeepLinks(callback: (ResultWrapper<Boolean>) -> Unit) {
        val data: Uri? = intent?.data

        data?.let {
            deepLinksHelper.handle(it, callback)
        } ?: callback.invoke(ResultWrapper.Success(true))
    }

}