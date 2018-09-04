package com.xiiilab.socialtest.api.github

import com.xiiilab.socialtest.api.ApiFactory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by XIII-th on 02.09.2018
 */
interface GithubApi {

    /**
     * https://developer.github.com/v3/activity/events/#list-events-performed-by-a-user
     * https://developer.github.com/v3/guides/traversing-with-pagination/#navigating-through-the-pages
     */
    @GET("/users/{username}/events")
    fun getUserEvents(@Path("username") username: String, @Query("page") page: Int = 1, @Query("per_page") count: Int):
            Call<List<UserEvent>>

    companion object {
        private const val BASE_URL = "https://api.github.com"
        fun get() = ApiFactory.create(GithubApi::class.java, BASE_URL, "GITHUB_API")
    }
}