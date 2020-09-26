package com.sabidos.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.Account
import com.sabidos.domain.Timeline
import com.sabidos.domain.WeeklyHits
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.*
import com.sabidos.infrastructure.extensions.*
import kotlinx.android.synthetic.main.content_home_layout.*
import kotlinx.android.synthetic.main.content_home_weekly_timers.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar.SATURDAY

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()

    private val adapter = TimelineAdapter(clickListener = { })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        today().getNext(SATURDAY).apply {
            simpleTimerCtComponent.endDate = this
            weeklyProgressTimer.endDate = this
        }

        myWeeklyChartComponent.currentWeekFilterCallback = {
            viewModel.getWeeklyHitsFor(today().getNext(SATURDAY).format())
        }

        myWeeklyChartComponent.lastWeekFilterCallback = {
            viewModel.getWeeklyHitsFor(today().getLast(SATURDAY).format())
        }

        timelineComponent.setup(adapter, nestedScrollView)
        timelineComponent.onLoadMore { nextPage ->
            viewModel.loadTimeline(nextPage)
        }

        viewModel.getCurrentAccount()
        viewModel.getWeeklyHitsFor(today().getNext(SATURDAY).format())
        viewModel.loadTimeline()

        viewModel.accountResource.observe(viewLifecycleOwner, Observer { bindAccountState(it) })
        viewModel.weeklyHitsResource.observe(
            viewLifecycleOwner,
            Observer { bindWeeklyHitsState(it) })
        viewModel.timelineResource.observe(viewLifecycleOwner, Observer { bindTimelineState(it) })
    }

    private fun bindAccountState(resource: Resource<Account?>?) = resource?.let {
        when (it.state) {
            Success -> setupAccountInformation(it.data)
        }
    }

    private fun bindWeeklyHitsState(resource: Resource<List<WeeklyHits>>?) = resource?.let {
        if (it.state != Loading) {
            myWeeklyChartComponent.stopLoading()
        }
        when (it.state) {
            Loading -> myWeeklyChartComponent.startLoading()
            Success -> myWeeklyChartComponent.setup(it.data)
            NetworkError -> {
                homeOverlayContentView.show()
                appBarLayout.setExpanded(false)
                homeOverlayContentView.showNetworkErrorWithRetry {
                    homeOverlayContentView.hide()
                    viewModel.getWeeklyHitsFor(today().getNext(SATURDAY).format())
                    viewModel.loadTimeline()
                }
            }
            else -> myWeeklyChartComponent.setupError {
                viewModel.getWeeklyHitsFor(
                    today().getNext(
                        SATURDAY
                    ).format()
                )
            }
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

    private fun setupAccountInformation(account: Account?) {
        homeStatusBar.username = account?.name
        homeStatusBar.userAvatar = account?.avatar
        levelInfoComponent.setup(account?.reputation?.level, account?.reputation?.stars)
    }

}