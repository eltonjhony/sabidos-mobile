package com.sabidos.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.WeeklyHits
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState.Loading
import com.sabidos.infrastructure.ResourceState.Success
import com.sabidos.infrastructure.extensions.format
import com.sabidos.infrastructure.extensions.getLast
import com.sabidos.infrastructure.extensions.getNext
import com.sabidos.infrastructure.extensions.today
import com.sabidos.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_performance.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar.SATURDAY

class MyPerformanceFragment : BaseFragment() {

    private val viewModel: MyPerformanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_performance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getWeeklyHitsFor(today().getNext(SATURDAY).format())

        myWeeklyChartComponent.currentWeekFilterCallback = {
            viewModel.getWeeklyHitsFor(today().getNext(SATURDAY).format())
        }

        myWeeklyChartComponent.lastWeekFilterCallback = {
            viewModel.getWeeklyHitsFor(today().getLast(SATURDAY).format())
        }

        viewModel.weeklyHitsResource.observe(
            viewLifecycleOwner,
            Observer { bindWeeklyHitsState(it) })
    }

    private fun bindWeeklyHitsState(resource: Resource<List<WeeklyHits>>?) = resource?.let {
        if (it.state != Loading) {
            myWeeklyChartComponent.stopLoading()
        }
        when (it.state) {
            Loading -> myWeeklyChartComponent.startLoading()
            Success -> myWeeklyChartComponent.setup(it.data)
            else -> myWeeklyChartComponent.setupError {
                viewModel.getWeeklyHitsFor(
                    today().getNext(
                        SATURDAY
                    ).format()
                )
            }
        }
    }

}