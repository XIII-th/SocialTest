package com.xiiilab.socialtest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.vm.GithubCommitListViewModel


class GithubCommitFragment : Fragment() {

    companion object {
        fun newInstance() = GithubCommitFragment()
    }

    private lateinit var mListViewModel: GithubCommitListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.github_commit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // use shared vm
        activity.let { mListViewModel = ViewModelProviders.of(this).get(GithubCommitListViewModel::class.java) }
    }

}
