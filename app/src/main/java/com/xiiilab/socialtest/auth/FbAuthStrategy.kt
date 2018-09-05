package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*

/**
 * Created by XIII-th on 04.09.2018
 */
object FbAuthStrategy : AbstractAuthStrategy() {

    private lateinit var mCallbackManager: CallbackManager

    override fun init(appContext: Context) {
        super.init(appContext)
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                mAuthResult.onNext(AuthResult.SUCCESS)
            }

            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException?) {
                mAuthResult.onNext(AuthResult.error{ error?.message })
            }
        })
    }

    override fun checkAuth(context: Context): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return super.checkAuth(context) || (accessToken != null && !accessToken.isExpired)
    }

    override fun startAuthFlow(activity: Activity) {
        super.startAuthFlow(activity)
        LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singleton("public_profile"))
    }

    override fun onAuthFlowResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onAuthFlowResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun logout() {
        super.logout()
        LoginManager.getInstance().logOut()
    }

    override fun loadUserInfo(): UserInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}