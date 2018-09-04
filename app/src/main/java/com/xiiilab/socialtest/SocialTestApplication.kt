package com.xiiilab.socialtest

import android.app.Application
import com.xiiilab.socialtest.auth.FbAbstractAuthStrategy
import com.xiiilab.socialtest.auth.GAbstractAuthStrategy
import com.xiiilab.socialtest.auth.VkAbstractAuthStrategy

/**
 * Created by XIII-th on 04.09.2018
 */
class SocialTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VkAbstractAuthStrategy.init(this)
        FbAbstractAuthStrategy.init(this)
        GAbstractAuthStrategy.init(this)
    }
}