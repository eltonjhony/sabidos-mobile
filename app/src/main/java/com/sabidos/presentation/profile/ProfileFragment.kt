package com.sabidos.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.Success
import com.sabidos.infrastructure.extensions.setCustomTabView
import com.sabidos.presentation.BaseFragment
import com.sabidos.presentation.profile.views.ProfileViewPagerAdapter
import com.sabidos.presentation.profile.views.ProfileViewPagerAdapter.Companion.ACCOUNT_MANAGEMENT_TAB_POSITION
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
        setupViewPager()

        viewModel.getCurrentAccount()
        viewModel.accountResource.observe(viewLifecycleOwner, Observer { bindAccountState(it) })
    }

    private fun setupViewPager() {
        val adapter = ProfileViewPagerAdapter(context, childFragmentManager)
        profileViewPager.adapter = adapter
        profileViewPager.setCurrentItem(ACCOUNT_MANAGEMENT_TAB_POSITION, true)
        tabLayout.setupWithViewPager(profileViewPager)
        tabLayout.setCustomTabView()
    }

    private fun bindAccountState(resource: Resource<Account?>?) = resource?.let {
        when (it.state) {
            Success -> setupProfile(it.data)
        }
    }

    private fun setupProfile(account: Account?) {
        account?.let {
            userProfileInfoComponent.setup(it)
            profileToolbarComponent.setupFor(it.avatar)
        }
    }

}