package com.xiiilab.socialtest.api.vk

import com.xiiilab.socialtest.api.ApiFactory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by XIII-th on 02.09.2018
 */
interface VkApi {

    /**
     * https://vk.com/dev/api_requests
     */
    @GET("/method/users.get?fields=photo_200")
    fun getUserInfo(@Query("user_ids") userId: String,
                    @Query("access_token") token: String,
                    @Query("v") apiVersion: String = "5.84"):
            Call<VkUserInfoResponse>

    companion object {
        private const val BASE_URL = "https://api.vk.com/method/"
        fun get() = ApiFactory.create(VkApi::class.java, BASE_URL, "VK_API")
    }
}