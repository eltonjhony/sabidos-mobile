package com.sabidos.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.domain.User
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState
import com.sabidos.infrastructure.extensions.goTo
import com.sabidos.presentation.BaseFragment
import com.sabidos.presentation.onboarding.OnboardingActivity
import com.sabidos.presentation.onboarding.OnboardingActivity.Companion.AUTHENTICATE_ANONYMOUS_PARAM
import kotlinx.android.synthetic.main.content_profile_layout.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrentUser()
        viewModel.getCurrentAccount()

        viewModel.currentUserResource.observe(viewLifecycleOwner, Observer { bindUserState(it) })
        viewModel.accountResource.observe(viewLifecycleOwner, Observer { bindAccountState(it) })
        viewModel.signOutResource.observe(viewLifecycleOwner, Observer { bindSignOutState(it) })

        userManagmtComponent.onLogoutClickLister = {
            viewModel.signOut()
        }

        userManagmtComponent.onLinkAccountClickListener = {
            val bundle = Bundle()
            bundle.putBoolean(
                AUTHENTICATE_ANONYMOUS_PARAM,
                true
            )
            activity?.goTo(OnboardingActivity::class.java, false, bundle = bundle)
        }

    }

    private fun bindUserState(resource: Resource<User?>?) = resource?.let {
        when (it.state) {
            ResourceState.Success -> setupUserDefinitions(it.data)
        }
    }

    private fun bindAccountState(resource: Resource<Account?>?) = resource?.let {
        when (it.state) {
            ResourceState.Success -> setupProfile(it.data)
        }
    }

    private fun bindSignOutState(resource: Resource<Boolean?>?) = resource?.let {
        when (it.state) {
            ResourceState.Success -> activity?.goTo(OnboardingActivity::class.java)
            else -> showGenericErrorDialog()
        }
    }

    private fun setupProfile(account: Account?) {
        account?.let {
            userProfileInfoComponent.setup(it)
            profileToolbarComponent.setupFor(it.avatar)
        }
    }

    private fun setupUserDefinitions(user: User?) {
        user?.let {
            userManagmtComponent.setup(it.isAnonymous)
        }
    }

}