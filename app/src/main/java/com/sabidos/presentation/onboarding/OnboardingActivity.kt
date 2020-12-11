package com.sabidos.presentation.onboarding

import android.os.Bundle
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.infrastructure.extensions.replaceFragmentSafely
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.BaseActivity
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.PERSONAL_INFO_STEP
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.PHONE_VERIFICATION_STEP
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.SIGN_IN_STEP
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.STEPS_COMPLETED
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.USER_AVATAR_STEP
import com.sabidos.presentation.splash.SplashActivity
import org.koin.android.viewmodel.ext.android.viewModel

class OnboardingActivity : BaseActivity() {

    private val viewModel: OnboardingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewModel.isAnonymousMigration = intent.getBooleanExtra(AUTHENTICATE_ANONYMOUS_PARAM, false)
        viewModel.defineStep(intent.getIntExtra(FIRST_STEP_NUMBER_PARAM, SIGN_IN_STEP))

        viewModel.currentStep.observe(this, Observer { step ->
            if (viewModel.isBackPressed) {
                return@Observer
            }

            runCatching {
                when (step) {
                    SIGN_IN_STEP -> replaceFragmentSafely(LoginFragment(), R.id.containerFragment)
                    PERSONAL_INFO_STEP -> replaceFragmentSafely(
                        PersonalInfoFragment(),
                        R.id.containerFragment
                    )
                    USER_AVATAR_STEP -> replaceFragmentSafely(
                        UserAvatarFragment(),
                        R.id.containerFragment
                    )
                    PHONE_VERIFICATION_STEP -> replaceFragmentSafely(
                        PhoneVerificationFragment(),
                        R.id.containerFragment
                    )
                    STEPS_COMPLETED -> goTo(SplashActivity::class.java)
                }
            }.onFailure {
                Logger.withTag(OnboardingActivity::class.java.simpleName).withCause(it)
            }
        })

    }

    override fun onBackPressed() {
        if (viewModel.isAnonymousMigration && viewModel.currentStep.value == SIGN_IN_STEP) {
            finish()
        } else if (viewModel.canGoBack()) {
            viewModel.goBackStep()
            super.onBackPressed()
        }
    }

    companion object {
        const val FIRST_STEP_NUMBER_PARAM = "FIRST_STEP_NUMBER"
        const val AUTHENTICATE_ANONYMOUS_PARAM = "AUTHENTICATE_ANONYMOUS"
    }

}