package com.sabidos.presentation.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sabidos.R
import com.sabidos.domain.RankingWrapper
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState
import com.sabidos.infrastructure.extensions.*
import com.sabidos.presentation.components.SimpleFilterComponent
import kotlinx.android.synthetic.main.content_ranking_layout.*
import kotlinx.android.synthetic.main.fragment_ranking.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class RankingFragment : Fragment() {

    private val viewModel: RankingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rankingToolbarComponent.setup(R.drawable.ic_ranking_icon)
        rankingTopsComponent.setup()
        setupFilter()

        viewModel.loadWeeklyRanking(today().getNext(Calendar.SATURDAY).format())
        rankingErrorComponent.onReloadListener { retryFilterRequest() }

        viewModel.rankingResource.observe(viewLifecycleOwner, Observer { bindRankingState(it) })
    }

    private fun bindRankingState(it: Resource<RankingWrapper>) {
        if (it.state != ResourceState.Loading) {
            rankingTopsComponent.showLoading(false)
        }
        when (it.state) {
            ResourceState.Loading -> rankingTopsComponent.showLoading()
            ResourceState.Success -> handleSuccess(it.data)
            ResourceState.GenericError -> handleError()
            ResourceState.NetworkError -> {
                rankingOverlayContentView.show()
                appBarLayout.setExpanded(false)
                rankingOverlayContentView.showNetworkErrorWithRetry {
                    rankingOverlayContentView.hide()
                    retryFilterRequest()
                }
            }
        }
    }

    private fun retryFilterRequest() {
        if (rankingFilterComponent.selectedOption == 1) {
            viewModel.loadWeeklyRanking(today().getLast(Calendar.SATURDAY).format())
        } else {
            viewModel.loadWeeklyRanking(today().getNext(Calendar.SATURDAY).format())
        }
    }

    private fun handleSuccess(data: RankingWrapper?) {
        rankingErrorComponent.hide()
        rankingTopsComponent.show()
        data?.let {
            rankingTopsComponent.podium = it.podium
            rankingTopsComponent.regularPositions = it.regularPositions
        }
    }

    private fun handleError() {
        rankingErrorComponent.show()
        rankingTopsComponent.hide()
    }

    private fun setupFilter() {
        rankingFilterComponent.firstOption = SimpleFilterComponent.FilterModel(
            getString(R.string.previous_week_long_label)
        ) {
            viewModel.loadWeeklyRanking(today().getLast(Calendar.SATURDAY).format())
        }

        rankingFilterComponent.secondOption = SimpleFilterComponent.FilterModel(
            getString(R.string.current_week_long_label), isDefault = true
        ) {
            viewModel.loadWeeklyRanking(today().getNext(Calendar.SATURDAY).format())
        }
    }

}