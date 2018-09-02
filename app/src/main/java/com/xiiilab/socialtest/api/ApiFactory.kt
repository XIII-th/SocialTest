package com.xiiilab.socialtest.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by XIII-th on 02.09.2018
 */
private object ApiFactory {

    fun <T : Class<T>> create(api : T, baseUrl : String, logTag : String) : T {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.d(logTag, it)
        })
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api::class.java)
    }
}