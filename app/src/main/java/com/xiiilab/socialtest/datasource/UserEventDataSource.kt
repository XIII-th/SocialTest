package com.xiiilab.socialtest.datasource

import androidx.paging.PageKeyedDataSource
import com.xiiilab.socialtest.api.github.GithubApi
import com.xiiilab.socialtest.api.github.UserEvent

/**
 * Created by XIII-th on 02.09.2018
 */
class UserEventDataSource(val query : String, val api : GithubApi) : PageKeyedDataSource<Int, UserEvent>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserEvent>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserEvent>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserEvent>) {
        // ignored, since we only ever append to our initial load
    }

}