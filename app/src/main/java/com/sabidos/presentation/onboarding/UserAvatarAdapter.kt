package com.sabidos.presentation.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.extensions.color
import com.sabidos.infrastructure.extensions.didSelect
import com.sabidos.infrastructure.extensions.load
import kotlinx.android.synthetic.main.avatar_choice_content_item.view.*

class UserAvatarAdapter(
    private var avatars: MutableList<UserAvatar> = mutableListOf(),
    val clickListener: (UserAvatar) -> Unit
) : RecyclerView.Adapter<UserAvatarAdapter.UserAvatarViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAvatarViewHolder {
        return UserAvatarViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.avatar_choice_content_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = avatars.size

    override fun onBindViewHolder(holder: UserAvatarViewHolder, position: Int) {
        val avatar = avatars[position]
        holder.bind(avatar)

        if (selectedPosition == position) {
            holder.select()
        } else {
            holder.unSelect()
        }

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            clickListener(avatar)
        }
    }

    fun addItems(items: List<UserAvatar>?) {
        items?.let {
            avatars.clear()
            avatars.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class UserAvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(userAvatar: UserAvatar) {

            userAvatar.apply {
                itemView.avatarIconView.load(this.imageUrl)
            }

        }

        fun select() {
            itemView.didSelect()
        }

        fun unSelect() {
            itemView.setBackgroundColor(
                itemView.context.color(
                    R.color.colorWhite
                )
            )
        }

    }

}