package com.sabidos.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sabidos.R
import com.sabidos.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal_info.*

class PersonalInfoFragment : BaseFragment() {

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.build(context)

        setupUi()
    }

    private fun setupUi() {

        nicknameInputGroup.setValue(viewModel.nickname)
        firstNameInputGroup.setValue(viewModel.name)

        firstNameInputGroup.onChange { checkToEnabledNextButton() }
        nicknameInputGroup.onChange { checkToEnabledNextButton() }

        nextButton.disabled()
        nextButton.onClickListener {
            viewModel.setupPersonalInfo(firstNameInputGroup.value, nicknameInputGroup.value)
            viewModel.nextStep()
        }

        checkToEnabledNextButton()
    }

    private fun checkToEnabledNextButton() {
        if (firstNameInputGroup.value.isNotBlank() && nicknameInputGroup.value.isNotBlank()) {
            nextButton.enable()
        } else {
            nextButton.disabled()
        }
    }

}