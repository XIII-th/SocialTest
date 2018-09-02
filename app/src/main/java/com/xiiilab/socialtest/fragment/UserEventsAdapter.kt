package com.xiiilab.socialtest.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.api.github.UserEvent
import com.xiiilab.socialtest.databinding.UserEventItemBinding

/**
 * Created by XIII-th on 02.09.2018
 */
class UserEventsAdapter : PagedListAdapter<UserEvent, UserEventViewHolder>(UserEventDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<UserEventItemBinding>(inflater, R.layout.user_event_item, parent, false)
        return UserEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserEventViewHolder, position: Int) {
        // avoid usage of item view model for simplicity
        // binding support nullable items as cleared sate
        holder.binding.userEvent = getItem(position)
    }


}