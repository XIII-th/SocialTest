package com.xiiilab.socialtest.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by XIII-th on 02.09.2018
 */
object ApiFactory {

    private val mInstances: MutableMap<Class<*>, Any> = HashMap()

    fun <T> create(api: Class<T>, baseUrl: String, logTag: String): T {
        synchronized(mInstances) {
            if (!mInstances.containsKey(api)) {
                val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    Log.d(logTag, it)
                })
                logger.level = HttpLoggingInterceptor.Level.BASIC

                val client = OkHttpClient.Builder()
                        .addInterceptor(logger)
                        .build()
                mInstances[api] = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client)
//                      .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(api) as Any
            }
            return mInstances[api] as T
        }
    }
}