package com.sabidos.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.domain.Category
import com.sabidos.domain.Timeline
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.*
import com.sabidos.presentation.category.CategoryHorizontalAdapter
import com.sabidos.presentation.common.StartToPlayCommand
import kotlinx.android.synthetic.main.content_home_layout.*
import kotlinx.android.synthetic.main.content_home_weekly_timers.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar.SATURDAY

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()

    private val adapter = TimelineAdapter(clickListener = { })

    private val browseAllAdapter = CategoryHorizontalAdapter(false) {
        StartToPlayCommand.playWithCategory(activity, it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

        today().getNext(SATURDAY).apply {
            simpleTimerCtComponent.endDate = this
            weeklyProgressTimer.endDate = this
        }

        timelineComponent.setup(adapter, nestedScrollView)
        timelineComponent.onLoadMore { nextPage ->
            viewModel.loadTimeline(nextPage)
        }

        viewModel.getCurrentAccount()
        viewModel.loadTimeline()
        viewModel.loadCategories()

        viewModel.accountResource.observe(viewLifecycleOwner, Observer { bindAccountState(it) })
        viewModel.timelineResource.observe(viewLifecycleOwner, Observer { bindTimelineState(it) })
        viewModel.categoriesResource.observe(viewLifecycleOwner, Observer { bindCategories(it) })
    }

    private fun setupUi() {
        categoriesContainer.hide()
        categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = browseAllAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun bindAccountState(resource: Resource<Account?>?) = resource?.let {
        when (it.state) {
            Success -> setupAccountInformation(it.data)
        }
    }

    private fun bindTimelineState(it: Resource<List<Timeline>>) {
        if (it.state != Loading) {
            timelineComponent.stopLoading()
        }
        when (it.state) {
            Loading -> timelineComponent.starLoading()
            Success -> adapter.updateItems(it.data)
            GenericError -> (activity as AppCompatActivity).showGenericErrorSnackBar()
        }
    }

    private fun bindCategories(resource: Resource<List<Category>>?) {
        resource?.let {
            when (it.state) {
                Success -> setupCategories(it.data)
                else -> categoriesContainer.hide()
            }
        }
    }

    private fun setupCategories(categories: List<Category>?) {
        categoriesContainer.show()
        browseAllAdapter.addItems(categories)
    }

    private fun setupAccountInformation(account: Account?) {
        homeStatusBar.username = account?.name
        homeStatusBar.userAvatar = account?.avatar
        levelInfoComponent.setup(account?.reputation?.level, account?.reputation?.stars)
    }

}