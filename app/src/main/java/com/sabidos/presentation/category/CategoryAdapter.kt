package com.sabidos.presentation.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.Category
import com.sabidos.infrastructure.extensions.dpToPx
import com.sabidos.infrastructure.extensions.load
import com.sabidos.infrastructure.extensions.loadAsDrawable
import com.sabidos.infrastructure.extensions.roundImage
import kotlinx.android.synthetic.main.category_choice_content_item.view.*
import kotlinx.android.synthetic.main.category_choice_secundary_content_item.view.*

class CategoryAdapter(
    private val isPrimary: Boolean = true,
    private var categories: MutableList<Category> = mutableListOf(),
    private val isGrid: Boolean = false,
    val clickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return if (isPrimary) {
            CategoryViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.category_choice_content_item,
                    parent,
                    false
                )
            )
        } else {
            CategoryViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.category_choice_secundary_content_item,
                    parent,
                    false
                )
            )
        }
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

            if (isGrid) {
                val padding = itemView.context.dpToPx(4f)
                itemView.setPadding(padding, padding, padding, padding)
            }

            if (isPrimary) {
                itemView.categoryIconView.loadAsDrawable(category.imageUrl) {
                    itemView.categoryIconView.roundImage(16f)
                }
            } else {
                itemView.categoryIconImageView.load(category.iconUrl)
                itemView.categoryName.text = category.description
            }
        }

    }

}