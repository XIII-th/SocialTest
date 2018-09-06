package com.xiiilab.socialtest.vm

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.api.github.GithubApi
import com.xiiilab.socialtest.api.github.UserEvent
import com.xiiilab.socialtest.datasource.UserEventDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class UserEventsListViewModel(private val api: GithubApi, private val config: PagedList.Config) : ViewModel() {
    private val mDisposables = CompositeDisposable()
    private val mError = PublishSubject.create<Int>()
    val mQuery: BehaviorSubject<String> = BehaviorSubject.create<String>()
    val mList: Flowable<PagedList<UserEvent>>

    init {
        mList = mQuery.debounce(800, TimeUnit.MILLISECONDS)
                .map(String::trim)
                .filter(String::isNotEmpty)
                .distinctUntilChanged()
                .switchMap(this::newRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { mError.onNext(R.string.unable_to_request_events) }
                .onErrorResumeNext(Observable.empty())
                .toFlowable(BackpressureStrategy.LATEST)
    }

    fun subscribeOnError(subscriber: (Int) -> Unit) {
        mDisposables.add(mError.subscribe(subscriber))
    }

    private fun newRequest(query: String): ObservableSource<PagedList<UserEvent>> {
        return RxPagedListBuilder(UserEventDataSourceFactory(query, api), config).buildObservable()
    }

    override fun onCleared() {
        mDisposables.clear()
    }
}
