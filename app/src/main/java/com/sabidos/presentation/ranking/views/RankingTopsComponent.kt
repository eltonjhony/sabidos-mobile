package com.sabidos.presentation.ranking.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.sabidos.R
import com.sabidos.domain.Ranking
import com.sabidos.infrastructure.extensions.hide
import com.sabidos.infrastructure.extensions.pump
import com.sabidos.infrastructure.extensions.show
import com.sabidos.presentation.ranking.RegularRankingAdapter
import com.sabidos.presentation.components.BaseComponent
import kotlinx.android.synthetic.main.sabidos_ranking_tops_component.view.*

class RankingTopsComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseComponent(
    R.layout.sabidos_ranking_tops_component,
    context = context,
    attrs = attrs,
    defStyleAttr = defStyleAttr
) {

    private val rankingAdapter = RegularRankingAdapter {

    }

    var podium: List<Ranking> = emptyList()
        set(value) {
            field = value
            setupPodium()
        }

    var regularPositions: List<Ranking> = emptyList()
        set(value) {
            field = value
            setupRegularPlayers()
        }
    
    fun setup() {
        loadingView.setup(true)
        rankingContainerLayout.hide()
    }

    fun showLoading(show: Boolean = true) {
        rankingContainerLayout.hide()
        if (show) {
            loadingView.show()
            loadingView.startAnimation()
        } else {
            loadingView.hide()
            loadingView.stopAnimation()
        }
    }

    private fun setupPodium() {
        rankingContainerLayout.show()
        podium.forEach {

            when (it.position) {
                1 -> {
                    configurePodiumFor(firstPositionComponent, it)
                    pumpPodium(firstPositionComponent, it)
                }
                2 -> {
                    configurePodiumFor(secondPositionComponent, it)
                    pumpPodium(secondPositionComponent, it)
                }
                3 -> {
                    configurePodiumFor(thirdPositionComponent, it)
                    pumpPodium(thirdPositionComponent, it)
                }
            }
        }
    }

    private fun pumpPodium(item: RankingTopsItemComponent, ranking: Ranking) {
        if (ranking.isMyPosition) {
            item.pump()
        }
    }

    private fun configurePodiumFor(component: RankingTopsItemComponent, ranking: Ranking) {
        component.apply {
            position = ranking.position
            userAvatar = ranking.avatar
            firstName = ranking.firstName
            nickname = ranking.nickname
            level = ranking.reputation.level
            stars = ranking.reputation.stars
            totalHits = ranking.totalHits
        }
    }

    private fun setupRegularPlayers() {
        rankingContainerLayout.show()
        val linearLayoutManager = LinearLayoutManager(context)
        regularRankingRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = rankingAdapter
            isNestedScrollingEnabled = false
        }
        rankingAdapter.addItems(regularPositions)
    }

}