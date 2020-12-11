package com.sabidos.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sabidos.R
import com.sabidos.data.remote.model.ErrorResponse.Companion.ERROR_NICKNAME_ALREADY_IN_USE
import com.sabidos.infrastructure.EventObserver
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal_info.*
import org.koin.android.viewmodel.ext.android.viewModel

class PersonalInfoFragment : BaseFragment() {

    private val viewModel: OnboardingViewModel by activityViewModels()
    private val personalInfoViewModel: PersonalInfoViewModel by viewModel()

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
        personalInfoViewModel.validateAccountResource.observe(
            viewLifecycleOwner,
            EventObserver { bindValidateAccountState(it) })
    }

    private fun bindValidateAccountState(resource: Resource<Void>) {
        if (resource.state != Loading) {
            loading.dismiss()
        }

        when (resource.state) {
            is Loading -> loading.show()
            is Success -> {
                viewModel.setupPersonalInfo(firstNameInputGroup.value, nicknameInputGroup.value)
                viewModel.nextStep()
            }
            is ApiError -> {
                when (resource.errorResponse?.code) {
                    ERROR_NICKNAME_ALREADY_IN_USE -> {
                        nicknameInputGroup.showInputErrorWith(
                            getString(
                                R.string.nickname_already_exists_error
                            )
                        )
                    }
                    else -> showGenericErrorDialog()
                }
            }
            else -> showGenericErrorDialog()
        }
    }

    private fun setupUi() {

        nicknameInputGroup.setValue(viewModel.nickname)
        firstNameInputGroup.setValue(viewModel.name)

        firstNameInputGroup.onChange { checkToEnabledNextButton() }
        nicknameInputGroup.onChange { checkToEnabledNextButton() }

        nextButton.disabled()
        nextButton.onClickListener {
            nicknameInputGroup.hideInputError()
            personalInfoViewModel.validateAccount(nicknameInputGroup.value)
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