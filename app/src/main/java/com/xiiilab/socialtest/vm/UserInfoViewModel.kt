package com.xiiilab.socialtest.vm

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiiilab.socialtest.AvatarService
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.auth.AbstractAuthService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by XIII-th on 05.09.2018
 */
class UserInfoViewModel : ViewModel() {
    private val mFirstName = MutableLiveData<String>()
    private val mLastName = MutableLiveData<String>()
    private val mAvatarPath = MutableLiveData<String>()
    private val mDisposables = CompositeDisposable()
    private val mError = PublishSubject.create<Int>()
    private var mAuthService: AbstractAuthService? = null

    @MainThread
    fun init(authService: AbstractAuthService) {
        if (mAuthService === null) {
            mFirstName.value = ""
            mLastName.value = ""
            mAuthService = authService
            mDisposables.add(authService.userInfo()
                    .doOnError { mError.onNext(R.string.unable_to_load_user_info) }
                    .onErrorComplete()
                    .subscribe { userInfo ->
                mFirstName.value = userInfo.firstName
                mLastName.value = userInfo.lastName
            })
            mDisposables.add(AvatarService.getAvatarPath(authService)
                    .doOnError { mError.onNext(R.string.unable_to_load_avatar) }
                    .onErrorComplete()
                    .subscribe(mAvatarPath::setValue))
        } else if (authService != mAuthService)
            throw IllegalArgumentException("Switching of auth strategies is not supported")
    }

    fun getFirstName(): LiveData<String> = mFirstName
    fun getLastName(): LiveData<String> = mLastName
    fun getAvatarPath(): LiveData<String> = mAvatarPath
    fun subscribeOnError(subscriber: (Int) -> Unit) {
        mDisposables.add(mError.subscribe(subscriber))
    }

    override fun onCleared() {
        mDisposables.clear()
    }
}