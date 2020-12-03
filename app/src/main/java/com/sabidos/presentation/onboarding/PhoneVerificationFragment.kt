package com.sabidos.presentation.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.clickableText
import com.sabidos.infrastructure.extensions.hideKeyboard
import com.sabidos.infrastructure.extensions.invisible
import com.sabidos.infrastructure.extensions.show
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.BaseFragment
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.SIGN_IN_STEP
import kotlinx.android.synthetic.main.fragment_phone_verification.*
import org.koin.android.viewmodel.ext.android.viewModel

class PhoneVerificationFragment : BaseFragment() {

    private val sharedViewModel: OnboardingViewModel by activityViewModels()
    private val phoneVerificationViewModel: PhoneVerificationViewModel by viewModel()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phone_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.build(context)
        verifyButton.disabled()
        configureResendCodeLink()

        goBackIconView.setOnClickListener {
            sharedViewModel.defineStep(SIGN_IN_STEP)
        }

        verificationCodeComponent.onBackPressedCallback = {
            sharedViewModel.defineStep(SIGN_IN_STEP)
        }

        verificationCodeComponent.setup()
        verificationCodeComponent.onCodeFilledCallback = {
            verifyButton.enable()
        }

        verificationCodeComponent.onCodeNotFilledCallback = {
            verifyButton.disabled()
        }

        verifyButton.onClickListener {
            view.hideKeyboard()
            phoneVerificationViewModel.signInWithPhoneNumber(verificationCodeComponent.code())
        }

        screenSubTitle.text = buildSubTitleMessage()

        phoneVerificationViewModel.signInWithPhoneNumberResource.observe(
            viewLifecycleOwner,
            Observer { bindSignWithPhoneNumberState(it) }
        )

        phoneVerificationViewModel.resendCodeResource.observe(
            viewLifecycleOwner,
            Observer { bindResendCodeState(it) }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    private fun bindResendCodeState(resource: Resource<Void>?) {
        resource?.let {
            if (it.state != Loading) {
                loading.dismiss()
            }
            when (it.state) {
                Loading -> loading.show()
                Success -> startResendCodeInterval()
                NetworkError -> showNetworkErrorDialog()
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun configureResendCodeLink() {

        resendCodeTextView.clickableText(
            getString(R.string.resent_code_long_text),
            getString(R.string.resent_code_clickable_text)
        ) {
            phoneVerificationViewModel.resendCode()
        }

        startResendCodeInterval()
    }

    private fun startResendCodeInterval() {
        resendCodeTextView.invisible()
        handler.postDelayed({

            runCatching {
                resendCodeTextView.show()
            }.onFailure {
                Logger.withTag(PhoneVerificationFragment::class.java.simpleName).withCause(it)
            }

        }, RESEND_CODE_INTERVAL_IN_MILLIS)
    }

    private fun bindSignWithPhoneNumberState(resource: Resource<Void>?) {
        resource?.let {
            if (it.state != Loading) {
                loading.dismiss()
            }
            when (it.state) {
                Loading -> loading.show()
                Success -> sharedViewModel.forceFinishStep()
                NetworkError -> showNetworkErrorDialog()
                AuthError -> showAuthError(it.authErrorResponse)
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun buildSubTitleMessage(): String =
        getString(
            R.string.phone_verification_sub_title,
            phoneVerificationViewModel.getMaskedPhone()
        )

    companion object {
        private const val RESEND_CODE_INTERVAL_IN_MILLIS = 6000L
    }

}