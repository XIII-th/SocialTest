package com.xiiilab.socialtest.datasource

import androidx.paging.PageKeyedDataSource
import com.xiiilab.socialtest.api.github.GithubApi
import com.xiiilab.socialtest.api.github.UserEvent
import java.io.IOException

/**
 * Created by XIII-th on 02.09.2018
 */
class UserEventDataSource(val query: String, val api: GithubApi) : PageKeyedDataSource<Int, UserEvent>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UserEvent>) {
        val items = load(1, params.requestedLoadSize)
        callback.onResult(items, null, nextPage(1, items.size, params.requestedLoadSize))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserEvent>) {
        val items = load(params.key, params.requestedLoadSize)
        callback.onResult(items, nextPage(params.key, items.size, params.requestedLoadSize))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserEvent>) {
        // ignored, since we only ever append to our initial load
    }

    private fun load(page: Int, count: Int): List<UserEvent> {
        val eventsQuery = api.getUserEvents(query, page, count)
        try {
            val response = eventsQuery.execute()
            return response.body().orEmpty()
        } catch (e: IOException) {
            TODO()
        }
    }

    private fun nextPage(page: Int, loaded: Int, required: Int): Int? = if (loaded < required) null else page + 1
}