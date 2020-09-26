package com.sabidos.presentation.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.Category
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.load
import kotlinx.android.synthetic.main.category_choice_content_item.view.*

class CategoryAdapter(
    private var categories: MutableList<Category> = mutableListOf(),
    val clickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_choice_content_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)

        if (selectedPosition == position) {
            holder.select()
        } else {
            holder.unSelect()
        }

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            clickListener(category)
        }
    }

    fun addItems(result: List<Category>?) {
        result?.let {
            categories.clear()
            categories.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(category: Category) {
            itemView.categoryIconView.load(category.imageUrl)
            itemView.categoryDescriptionTextView.text = category.description
        }

        fun select() {
            itemView.setBackgroundColor(
                itemView.context.color(
                    R.color.colorPrimary
                )
            )
        }

        fun unSelect() {
            itemView.setBackgroundColor(
                itemView.context.color(
                    R.color.colorBackgroundLight
                )
            )
        }

    }

}