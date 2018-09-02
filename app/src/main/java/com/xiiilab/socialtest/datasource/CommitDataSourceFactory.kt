package com.xiiilab.socialtest.datasource

import androidx.paging.DataSource
import com.xiiilab.socialtest.api.GithubApi

/**
 * Created by XIII-th on 02.09.2018
 */
class CommitDataSourceFactory(val query : String, val api : GithubApi) : DataSource.Factory<Int, Commit>() {
    override fun create(): DataSource<Int, Commit> {
        return CommitDataSource(query, api)
    }
}