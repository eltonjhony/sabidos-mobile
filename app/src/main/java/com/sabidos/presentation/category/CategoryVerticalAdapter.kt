package com.sabidos.presentation.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.Category
import com.sabidos.infrastructure.extensions.loadAsDrawable
import com.sabidos.infrastructure.extensions.roundImage
import kotlinx.android.synthetic.main.category_choice_vertical_item.view.*

class CategoryVerticalAdapter(
    private var categories: MutableList<Category> = mutableListOf(),
    val clickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryVerticalAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_choice_vertical_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
        holder.itemView.setOnClickListener {
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
            itemView.categoryIconView.loadAsDrawable(category.imageUrl) {
                itemView.categoryIconView.roundImage(16f)
            }
        }

    }

}