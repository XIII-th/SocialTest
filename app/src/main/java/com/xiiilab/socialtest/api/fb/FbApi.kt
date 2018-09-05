package com.xiiilab.socialtest.api.fb

import com.xiiilab.socialtest.api.ApiFactory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by XIII-th on 05.09.2018
 */
interface FbApi {

    /**
     * https://developers.facebook.com/docs/graph-api/reference/v3.1/user
     */
    @GET("/v3.1/{user_id}?fields=first_name,last_name")
    fun getUserInfo(@Path("user_id") userId: String, @Query("access_token") token: String): Call<FbUser>

    companion object {
        private const val BASE_URL = "https://graph.facebook.com/"
        fun get() = ApiFactory.create(FbApi::class.java, BASE_URL, "FB_API")
    }
}