package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError

/**
 * Created by XIII-th on 04.09.2018
 */
object VkAuthStrategy : AbstractAuthStrategy() {

    override fun init(appContext: Context) {
        super.init(appContext)
        VKSdk.initialize(appContext)
    }

    override fun checkAuth(context: Context): Boolean {
        return super.checkAuth(context) || VKSdk.isLoggedIn()
    }

    override fun startAuthFlow(activity: Activity) {
        super.startAuthFlow(activity)
        VKSdk.login(activity)
    }

    override fun onAuthFlowResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onAuthFlowResult(requestCode, resultCode, data)
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken?) {
                mAuthResult.onNext(AuthResult.SUCCESS)
            }

            override fun onError(error: VKError?) {
                mAuthResult.onNext(AuthResult.error{ error?.errorMessage })
            }
        })
    }

    override fun logout() {
        super.logout()
        VKSdk.logout()
    }
}