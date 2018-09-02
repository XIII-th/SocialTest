package com.xiiilab.socialtest.fragment

import androidx.recyclerview.widget.DiffUtil
import com.xiiilab.socialtest.api.github.UserEvent

/**
 * Created by XIII-th on 02.09.2018
 */
object UserEventDiffCallback : DiffUtil.ItemCallback<UserEvent>() {

    override fun areItemsTheSame(oldItem: UserEvent, newItem: UserEvent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserEvent, newItem: UserEvent): Boolean {
        return oldItem == newItem
    }
}