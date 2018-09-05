package com.xiiilab.socialtest.vm

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiiilab.socialtest.AvatarService
import com.xiiilab.socialtest.auth.AbstractAuthService
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by XIII-th on 05.09.2018
 */
class UserInfoViewModel : ViewModel() {
    private val mFirstName = MutableLiveData<String>()
    private val mLastName = MutableLiveData<String>()
    private val mAvatarPath = MutableLiveData<String>()
    private val mDisposables = CompositeDisposable()
    private var mAuthService: AbstractAuthService? = null

    @MainThread
    fun init(authService: AbstractAuthService) {
        mFirstName.value = ""
        mLastName.value = ""
        if (mAuthService === null) {
            mAuthService = authService
            mDisposables.add(authService.userInfo().subscribe { userInfo ->
                mFirstName.value = userInfo.firstName
                mLastName.value = userInfo.lastName
            })
            mDisposables.add(AvatarService.getAvatarPath(authService).subscribe(System.out::println))
        } else if (authService != mAuthService)
            throw IllegalArgumentException("Switching of auth strategies is not supported")
    }

    fun getFirstName(): LiveData<String> = mFirstName
    fun getLastName(): LiveData<String> = mLastName
    fun getAvatarPath(): LiveData<String> = mAvatarPath

    override fun onCleared() {
        mDisposables.clear()
    }
}