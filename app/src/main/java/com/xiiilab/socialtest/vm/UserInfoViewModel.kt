package com.xiiilab.socialtest.vm

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiiilab.socialtest.auth.AbstractAuthStrategy
import io.reactivex.disposables.Disposable

/**
 * Created by XIII-th on 05.09.2018
 */
class UserInfoViewModel : ViewModel() {
    private val mFirstName = MutableLiveData<String>()
    private val mLastName = MutableLiveData<String>()
    private lateinit var mUserInfoDisposable: Disposable
    private var mAuthStrategy: AbstractAuthStrategy? = null

    @MainThread
    fun init(authStrategy: AbstractAuthStrategy) {
        mFirstName.value = ""
        mLastName.value = ""
        if (mAuthStrategy === null) {
            mAuthStrategy = authStrategy
            mUserInfoDisposable = mAuthStrategy!!.userInfo().subscribe { userInfo ->
                mFirstName.value = userInfo.firstName
                mLastName.value = userInfo.lastName
            }
        } else if (authStrategy != mAuthStrategy)
            throw IllegalArgumentException("Switching of auth strategies is not supported")
    }

    fun getFirstName(): LiveData<String> = mFirstName
    fun getLastName(): LiveData<String> = mLastName

    override fun onCleared() {
        mUserInfoDisposable.dispose()
    }
}