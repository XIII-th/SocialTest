package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by XIII-th on 04.09.2018
 */
abstract class AbstractAuthStrategy {
    protected companion object {
        const val TAG = "AUTH_STRATEGY"
    }

    protected val mAuthResult: PublishSubject<AuthResult> = PublishSubject.create()

    fun subscribe(consumer: (AuthResult) -> Unit): Disposable {
        return mAuthResult.subscribe(consumer)
    }

    open fun init(appContext: Context) {
        Log.d(TAG, "Initialisation of $javaClass")
    }

    open fun checkAuth(): Boolean {
        Log.d(TAG, "Check auth for $javaClass")
        return false
    }

    open fun startAuthFlow(activity: Activity) {
        Log.d(TAG, "Start auth flow of $javaClass")
    }

    open fun onAuthFlowResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d(TAG, "Completion of auth flow for $javaClass")
    }

    open fun logout() {
        Log.d(TAG, "Logout of $javaClass")
    }
}