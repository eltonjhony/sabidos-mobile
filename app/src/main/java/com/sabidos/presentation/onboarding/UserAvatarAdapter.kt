package com.sabidos.presentation.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sabidos.R
import com.sabidos.domain.UserAvatar
import com.sabidos.infrastructure.extensions.*
import com.sabidos.presentation.components.UpdateAvatarComponent
import kotlinx.android.synthetic.main.avatar_choice_content_item.view.*

class UserAvatarAdapter(
    private var avatars: MutableList<UserAvatar> = mutableListOf(),
    val clickListener: (UserAvatar) -> Unit,
    val onGalleryPickerListener: () -> GalleryPickerListener
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

    override fun getItemCount(): Int = avatars.size + 1

    override fun onBindViewHolder(holder: UserAvatarViewHolder, position: Int) {

        if (position == 0) {
            holder.bind()
            holder.itemView.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
            }
        } else {
            val avatar = avatars[position - 1]
            holder.bind(avatar)
            holder.itemView.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                clickListener(avatar)
            }
        }

        if (selectedPosition == position) {
            holder.select()
        } else {
            holder.unSelect()
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
            itemView.avatarIconView.show()
            itemView.updateAvatarComponent.hide()
            userAvatar.apply {
                itemView.avatarIconView.load(imageUrl)
            }
        }

        fun bind() {
            itemView.updateAvatarComponent.show()
            itemView.avatarIconView.hide()
            onGalleryPickerListener.invoke().setup(itemView.updateAvatarComponent)
        }
        
        fun select() {
            itemView.didSelect()
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

interface GalleryPickerListener {
    fun setup(component: UpdateAvatarComponent?)
}
