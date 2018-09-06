package com.xiiilab.socialtest

import android.app.Application
import com.xiiilab.socialtest.auth.AuthServiceLocator
import com.xiiilab.socialtest.auth.FbAuthService
import com.xiiilab.socialtest.auth.GAuthService
import com.xiiilab.socialtest.auth.VkAuthService

/**
 * Created by XIII-th on 04.09.2018
 */
class SocialTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AuthServiceLocator.registerServices(this, VkAuthService, GAuthService, FbAuthService)
        AvatarService.init(this)
    }
}