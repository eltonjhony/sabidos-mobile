package com.sabidos.presentation.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.Ranking
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.drawable
import kotlinx.android.synthetic.main.sabidos_ranking_regular_item.view.*

class RegularRankingAdapter(
    private var ranking: MutableList<Ranking> = mutableListOf(),
    val clickListener: (Ranking) -> Unit
) : RecyclerView.Adapter<RegularRankingAdapter.RankingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        return RankingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sabidos_ranking_regular_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = ranking.size

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = ranking[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { clickListener(item) }
    }

    fun addItems(results: List<Ranking>?) {
        results?.let {
            ranking.clear()
            ranking.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: Ranking) {

            model.apply {
                itemView.positionLabel.text = "$position º"
                itemView.playerImageView.userAvatar = avatar
                itemView.playerNameTextView.text = "$firstName - $nickname"
                itemView.levelStatusComponent.level = reputation.level
                itemView.levelStarsComponent.stars = reputation.stars
                itemView.hitsValueLabel.text = "$totalHits"

                if (isMyPosition) {
                    itemView.background =
                        itemView.context.drawable(R.drawable.card_ranking_top_background)
                    itemView.positionLabel.setTextColor(itemView.context.color(R.color.colorWhite))
                    itemView.playerNameTextView.setTextColor(
                        itemView.context.color(
                            R.color.colorWhite
                        )
                    )
                    itemView.hitsValueLabel.setTextColor(
                        itemView.context.color(
                            R.color.colorWhite
                        )
                    )
                }

            }

        }
    }

}