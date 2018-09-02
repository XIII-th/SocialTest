package com.xiiilab.socialtest.datasource

import androidx.paging.PositionalDataSource
import com.xiiilab.socialtest.api.GithubApi

/**
 * Created by XIII-th on 02.09.2018
 */
class CommitDataSource(val query : String, val api : GithubApi) : PositionalDataSource<Commit>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Commit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Commit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}