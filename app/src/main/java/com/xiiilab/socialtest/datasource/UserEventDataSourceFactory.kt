package com.xiiilab.socialtest.datasource

import androidx.paging.DataSource
import com.xiiilab.socialtest.api.github.GithubApi
import com.xiiilab.socialtest.api.github.UserEvent

/**
 * Created by XIII-th on 02.09.2018
 */
class UserEventDataSourceFactory(val query: String, val api: GithubApi) : DataSource.Factory<Int, UserEvent>() {
    override fun create(): DataSource<Int, UserEvent> {
        return UserEventDataSource(query, api)
    }
}