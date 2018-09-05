package com.xiiilab.socialtest.auth

import android.content.Context

/**
 * Created by XIII-th on 05.09.2018
 */
object AuthServiceLocator {
    private val mStrategies: MutableMap<String, AbstractAuthStrategy> = HashMap()

    fun registerStrategies(appContext: Context, vararg strategies: AbstractAuthStrategy) {
        for (strategy in strategies) {
            mStrategies[strategy.key()] = strategy
            strategy.init(appContext)
        }
    }

    operator fun get(key: String): AbstractAuthStrategy {
        return mStrategies[key] ?: throw IllegalArgumentException("Auth strategy for key $key is not registered")
    }

    fun getAll(): Array<AbstractAuthStrategy> {
        return mStrategies.values.toTypedArray()
    }
}