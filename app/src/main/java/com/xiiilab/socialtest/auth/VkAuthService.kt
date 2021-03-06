package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.xiiilab.socialtest.api.vk.VkApi
import com.xiiilab.socialtest.api.vk.VkUser
import io.reactivex.Maybe

/**
 * Created by XIII-th on 04.09.2018
 */
object VkAuthService : AbstractAuthService() {

    private val mApi by lazy { VkApi.get() }

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
                mAuthResult.onNext(
                        if (error?.errorCode == VKError.VK_CANCELED) AuthResult.CANCEL
                        else AuthResult.error { error?.errorMessage })
            }
        })
    }

    override fun logout() {
        super.logout()
        VKSdk.logout()
    }

    override fun loadUserInfo(): UserInfo? = loadUserInfo { info -> UserInfo(info.first_name, info.last_name) }

    override fun avatarUrl(): Maybe<String> = Maybe.fromCallable { loadUserInfo(VkUser::photo_200) }

    override fun getServiceName(): String = "VK$SERVICE_NAME_SUFFIX"

    private fun <T> loadUserInfo(func: (VkUser) -> T?): T? {
        return VKAccessToken.currentToken()?.run {
            val response = mApi.getUserInfo(userId, accessToken).execute()

            response.body()?.let {
                val info = if (it.response.size != 1)
                    throw IllegalStateException("Unexpected user info count ${it.response.size}")
                else it.response[0]
                func.invoke(info)
            } ?: throw Exception(getEmptyResponseErrorMessage("vk"))
        }
    }
}