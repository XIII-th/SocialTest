package com.xiiilab.socialtest.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.xiiilab.socialtest.api.github.GithubApi

/**
 * Created by XIII-th on 02.09.2018
 */
object GithubVmFactory : ViewModelProvider.Factory {
    private const val PAGE_SIZE = 30

    private val mGithubApi = GithubApi.get()
    private val mPagingConfig = PagedList.Config.Builder().
            setPageSize(PAGE_SIZE).
            setPrefetchDistance(PAGE_SIZE * 2).
            setEnablePlaceholders(true).
            build()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass === UserEventsListViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            UserEventsListViewModel(mGithubApi, mPagingConfig) as T
        else
            throw IllegalStateException("Unexpected vm class")
    }
}