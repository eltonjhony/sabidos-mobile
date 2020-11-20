package com.sabidos.presentation.onboarding

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.infrastructure.Constants
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.focusOnView
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.helpers.SignInPrefsHelper
import com.sabidos.infrastructure.logging.Logger
import com.sabidos.presentation.BaseFragment
import com.sabidos.presentation.onboarding.OnboardingViewModel.Companion.PHONE_VERIFICATION_STEP
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.sabidos_form_text_component.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private val sharedViewModel: OnboardingViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by viewModel()

    private val signInPrefsHelper: SignInPrefsHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.build(context)

        if (sharedViewModel.isAnonymousMigration) {
            signFooterComponent.hide()
        }

        signFooterComponent.onAnonymouslySignClickCallback = {
            loginViewModel.signInAnonymously()
        }

        emailTextComponent.onChange {
            emailTextComponent.hideInputError()
        }

        signInEmailButton.setOnClickListener {
            loginViewModel.signInWithEmailLink(emailTextComponent.value)
        }

        phoneTextComponent.onChange {
            phoneTextComponent.hideInputError()
            handlePhoneChange(it)
        }

        phoneTextComponent.onFocus { hasFocus ->
            if (hasFocus) {
                scrollView.focusOnView(signFooterComponent, delay = 300)
            }
        }

        signInPhoneButton.setOnClickListener {
            loginViewModel.verifyPhoneNumber(phoneTextComponent.value)
        }

        loginViewModel.signInAnonymouslyResource.observe(
            viewLifecycleOwner,
            Observer { bindSignInAnonymouslyState(it) })

        loginViewModel.signInWithEmailLinkResource.observe(
            viewLifecycleOwner,
            Observer { bindSignInWithEmailLink(it) })

        loginViewModel.verifyPhoneNumberResource.observe(
            viewLifecycleOwner,
            Observer { bindVerifyPhoneNumberState(it) }
        )

    }

    private fun bindSignInAnonymouslyState(resource: Resource<Void>?) {
        resource?.let {
            if (it.state != Loading) {
                loading.dismiss()
            }
            when (it.state) {
                Loading -> loading.show()
                Success -> sharedViewModel.forceFinishStep()
                NetworkError -> showNetworkErrorDialog()
                ApiError -> showApiError(it.errorResponse)
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun bindSignInWithEmailLink(resource: Resource<Void>?) {
        resource?.let {
            if (it.state != Loading) {
                loading.dismiss()
            }
            when (it.state) {
                Loading -> loading.show()
                Success -> {
                    showAnimationDialog(
                        Constants.Animation.MAIL_SENT_ANIMATION_PATH,
                        getString(R.string.mail_sent_title),
                        spanned = getEmailSentMessageFormatted()
                    )
                    emailTextComponent.setValue()
                }
                NetworkError -> showNetworkErrorDialog()
                ApiError -> showApiError(it.errorResponse)
                ValidationError -> showEmailValidationError()
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun bindVerifyPhoneNumberState(resource: Resource<Void>?) {
        resource?.let {
            if (it.state != Loading) {
                loading.dismiss()
            }
            when (it.state) {
                Loading -> loading.show()
                Success -> sharedViewModel.defineStep(PHONE_VERIFICATION_STEP)
                NetworkError -> showNetworkErrorDialog()
                ValidationError -> showPhoneValidationError()
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun handlePhoneChange(phone: String) {
        val formattedPhone = loginViewModel.getFormattedPhoneNumber(phone)
        if (phone == formattedPhone) return
        phoneTextComponent.setValue(formattedPhone)
        phoneTextComponent.formTextInputField.setSelection(phoneTextComponent.formTextInputField.text!!.length)
    }

    private fun showEmailValidationError() {
        emailTextComponent.showInputErrorWith(getString(R.string.email_validation_message))
    }

    private fun showPhoneValidationError() {
        phoneTextComponent.showInputErrorWith(getString(R.string.phone_number_validation_error_message))
    }

    private fun getEmailSentMessageFormatted(): SpannableString {
        val email = signInPrefsHelper.getEmail()
        val firstDescription = getString(R.string.mail_sent_description_start)
        val secondDescription = getString(R.string.mail_sent_description_end, email)

        val spannable = SpannableString("$firstDescription $secondDescription")
        val characterStyle: CharacterStyle = object : CharacterStyle() {
            override fun updateDrawState(p0: TextPaint?) {
                context?.let { context ->
                    p0?.typeface = Typeface.create(p0?.typeface, Typeface.BOLD)
                    p0?.color = context.color(R.color.colorPrimary)
                }
            }
        }

        runCatching {
            spannable.setSpan(
                characterStyle,
                firstDescription.length,
                (firstDescription.length + (email?.length ?: 0) + 1),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }.onFailure {
            Logger.withTag(LoginFragment::class.java.simpleName).withCause(it)
        }

        return spannable
    }

}