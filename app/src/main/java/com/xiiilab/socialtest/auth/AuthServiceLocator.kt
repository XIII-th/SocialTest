package com.xiiilab.socialtest.auth

import android.content.Context

/**
 * Created by XIII-th on 05.09.2018
 */
object AuthServiceLocator {

    private val mServicesMap: MutableMap<String, AbstractAuthService> = HashMap()

    fun registerServices(appContext: Context, vararg services: AbstractAuthService) {
        for (authService in services) {
            if (mServicesMap.containsKey(authService.getServiceName()))
                throw IllegalArgumentException("Duplication of service name ${authService.getServiceName()}")
            mServicesMap[authService.getServiceName()] = authService
            authService.init(appContext)
        }
    }

    operator fun get(serviceName: String): AbstractAuthService = mServicesMap[serviceName]
            ?: throw IllegalArgumentException("Auth service for key $serviceName is not registered")

    fun getAll(): Array<AbstractAuthService> = mServicesMap.values.toTypedArray()
}