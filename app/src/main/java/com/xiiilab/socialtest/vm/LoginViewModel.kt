package com.xiiilab.socialtest.vm

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xiiilab.socialtest.auth.AbstractAuthService
import com.xiiilab.socialtest.auth.AuthResult
import com.xiiilab.socialtest.auth.AuthServiceLocator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by XIII-th on 06.09.2018
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val mDisposables = CompositeDisposable()
    private val mInProgress = MutableLiveData<Boolean>()
    private val mAuthResult = MutableLiveData<AuthResult>()
    private var mAuthService: AbstractAuthService? = null
    private val mAuthInitialisationFlow = Observable.fromArray(*AuthServiceLocator.getAll())
            .subscribeOn(Schedulers.io())
            // searching for service where user is signed in
            .skipWhile { authService -> !authService.checkAuth(application) }
            .firstElement()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { mInProgress.value = true }
            .doOnComplete { mInProgress.value = false }

    init {
        mDisposables.add(mAuthInitialisationFlow.subscribe {
            mAuthService = it
            mAuthResult.value = AuthResult.SUCCESS
        })
    }

    fun isInProgress(): LiveData<Boolean> = mInProgress
    fun getAuthResult(): LiveData<AuthResult> = mAuthResult

    @MainThread
    fun authWith(activity: Activity, authService: AbstractAuthService) {
        mInProgress.value = true
        mAuthService = authService
        mDisposables.add(authService.subscribe {
            mInProgress.value = false
            mAuthResult.value = it
        })
        authService.startAuthFlow(activity)
    }

    @MainThread
    fun onAuthFlowResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mAuthService?.onAuthFlowResult(requestCode, resultCode, data)
    }

    fun getServiceName(): String? {
        return mAuthService?.getServiceName()
    }

    public override fun onCleared() {
        mDisposables.clear()
        mInProgress.value = false
        mAuthResult.value = null
    }
}