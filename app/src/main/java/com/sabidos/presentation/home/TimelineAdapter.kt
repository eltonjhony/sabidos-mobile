package com.sabidos.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.Timeline
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.drawable
import com.sabidos.infrastructure.extensions.format
import com.sabidos.infrastructure.extensions.toDate
import kotlinx.android.synthetic.main.sabidos_timeline_item.view.*

class TimelineAdapter(
    private var answeredQuestions: MutableList<Timeline> = mutableListOf(),
    val clickListener: (Timeline) -> Unit
) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sabidos_timeline_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = answeredQuestions.size

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val timeline = answeredQuestions[position]
        holder.bind(timeline)
        holder.itemView.setOnClickListener { clickListener(timeline) }
    }

    fun updateItems(results: List<Timeline>?) {
        results?.let {
            val positionStart = answeredQuestions.size
            answeredQuestions.addAll(it)
            notifyItemRangeInserted(positionStart, it.size)
        }
    }

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(timeline: Timeline) {

            timeline.apply {

                if (this.isCorrect) {
                    itemView.answerIconView.setImageDrawable(
                        itemView.context.drawable(
                            R.drawable.ic_baseline_check_circle
                        )
                    )
                } else {
                    itemView.answerIconView.setImageDrawable(
                        itemView.context.drawable(
                            R.drawable.ic_baseline_wrong_circle
                        )
                    )
                }

                itemView.questionTextView.text = this.description
                itemView.categoryTextView.text = this.category.description
                itemView.categoryTextView.setTextColor(itemView.context.color(R.color.colorPrimary))
                itemView.answerDateTextView.text = this.date.toDate()?.format("dd MMMM")
            }

        }
    }

}