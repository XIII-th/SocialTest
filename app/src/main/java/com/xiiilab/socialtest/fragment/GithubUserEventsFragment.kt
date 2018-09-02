package com.xiiilab.socialtest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.databinding.GithubUserEventsFragmentBinding
import com.xiiilab.socialtest.vm.GithubVmFactory
import com.xiiilab.socialtest.vm.UserEventsListViewModel
import io.reactivex.disposables.CompositeDisposable


class GithubUserEventsFragment : Fragment() {

    private val mDisposables = CompositeDisposable()
    private val mAdapter = UserEventsAdapter()

    companion object {
        fun newInstance() = GithubUserEventsFragment()
    }

    private lateinit var mListViewModel: UserEventsListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<GithubUserEventsFragmentBinding>(
                layoutInflater, R.layout.github_user_events_fragment, container, false)
        binding.userEventsList.adapter = mAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // use shared vm
        activity?.let {
            mListViewModel = ViewModelProviders.of(it, GithubVmFactory).get(UserEventsListViewModel::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        mDisposables.add(mListViewModel.mList.subscribe(mAdapter::submitList))
    }

    override fun onStop() {
        super.onStop()
        mDisposables.clear()
    }
}
