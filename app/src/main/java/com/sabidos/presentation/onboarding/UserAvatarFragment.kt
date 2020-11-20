package com.sabidos.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_user_avatar.*
import kotlinx.android.synthetic.main.fragment_user_avatar.finishButton
import kotlinx.android.synthetic.main.fragment_user_avatar.loadingView
import kotlinx.android.synthetic.main.fragment_user_avatar.scrollView
import org.koin.android.viewmodel.ext.android.viewModel

class UserAvatarFragment : BaseFragment() {

    private val avatarAdapter = UserAvatarAdapter {
        finishButton.enable()
        viewModel.setupUserAvatar(it)
    }

    private val viewModel: OnboardingViewModel by activityViewModels()
    private val avatarViewModel: UserAvatarViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_avatar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.build(context)
        loadingView.setup(true)
        finishButton.disabled()
        setupAvatarRecyclerView()
        setupObservers()

        avatarViewModel.getAllAvatars()

        goBackIconView.setOnClickListener {
            activity?.onBackPressed()
        }

        finishButton.onClickListener {
            viewModel.completeUserProfile()
        }
    }

    private fun setupObservers() {
        viewModel.completeProfileResource.observe(viewLifecycleOwner, Observer { bindState(it) })
        avatarViewModel.avatarsResource.observe(viewLifecycleOwner, Observer { bindAvatars(it) })
    }

    private fun bindAvatars(resource: Resource<List<UserAvatar>>?) {
        resource?.let {
            if (it.state != Loading) {
                loading(false)
            }
            when (it.state) {
                Loading -> loading(true)
                Success -> avatarAdapter.addItems(it.data)
                NetworkError -> showNetworkErrorDialog()
                else -> {
                    showGenericErrorDialog()
                    finishButton.enable()
                }
            }
        }
    }

    private fun bindState(resource: Resource<Void>?) {
        resource?.let {
            if (it.state != Loading) {
                loading.dismiss()
            }
            when (it.state) {
                Loading -> loading.show()
                Success -> viewModel.nextStep()
                NetworkError -> showNetworkErrorDialog()
                ApiError -> showApiError(it.errorResponse)
                else -> showGenericErrorDialog()
            }
        }
    }

    private fun setupAvatarRecyclerView() {
        avatarRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = avatarAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            scrollView.hide()
            loadingView.show()
            loadingView.startAnimation()
        } else {
            scrollView.show()
            loadingView.hide()
            loadingView.stopAnimation()
        }
    }

}