package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xiiilab.socialtest.AvatarService
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by XIII-th on 04.09.2018
 */
abstract class AbstractAuthService {
    protected companion object {
        const val SERVICE_NAME_SUFFIX = "_AUTH_SERVICE"
    }

    protected val mAuthResult: PublishSubject<AuthResult> = PublishSubject.create()

    fun subscribe(consumer: (AuthResult) -> Unit): Disposable {
        return mAuthResult.subscribe(consumer)
    }

    open fun init(appContext: Context) {
        Log.d(getServiceName(), "Initialisation of $javaClass")
    }

    open fun checkAuth(context: Context): Boolean {
        Log.d(getServiceName(), "Check auth for $javaClass")
        return false
    }

    open fun startAuthFlow(activity: Activity) {
        Log.d(getServiceName(), "Start auth flow of $javaClass")
    }

    open fun onAuthFlowResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(getServiceName(), "Completion of auth flow for $javaClass")
    }

    open fun logout() {
        Log.d(getServiceName(), "Logout of $javaClass")
        AvatarService.removeAvatar(getServiceName())
    }

    fun userInfo(): Maybe<UserInfo> {
        // TODO: Cache user info
        return Maybe.fromCallable { loadUserInfo() }.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
    }

    abstract fun avatarUrl(): Maybe<String>

    abstract fun getServiceName(): String

    protected fun getEmptyResponseErrorMessage(apiName: String) = "User info $apiName request return empty response"

    protected abstract fun loadUserInfo(): UserInfo
}