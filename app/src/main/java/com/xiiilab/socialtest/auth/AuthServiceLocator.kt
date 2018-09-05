package com.xiiilab.socialtest.auth

import android.content.Context

/**
 * Created by XIII-th on 05.09.2018
 */
object AuthServiceLocator {

    private val mServicesMap: MutableMap<String, AbstractAuthService> = HashMap()

    fun registerStrategies(appContext: Context, vararg services: AbstractAuthService) {
        for (strategy in services) {
            mServicesMap[strategy.getServiceName()] = strategy
            strategy.init(appContext)
        }
    }

    operator fun get(serviceName: String): AbstractAuthService = mServicesMap[serviceName] ?:
        throw IllegalArgumentException("Auth strategy for key $serviceName is not registered")

    fun getAll(): Array<AbstractAuthService> = mServicesMap.values.toTypedArray()
}