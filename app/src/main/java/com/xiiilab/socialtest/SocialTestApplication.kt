package com.xiiilab.socialtest

import android.app.Application
import com.xiiilab.socialtest.auth.FbAuthStrategy
import com.xiiilab.socialtest.auth.GAuthStrategy
import com.xiiilab.socialtest.auth.VkAuthStrategy

/**
 * Created by XIII-th on 04.09.2018
 */
class SocialTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VkAuthStrategy.init(this)
        FbAuthStrategy.init(this)
        GAuthStrategy.init(this)
    }
}