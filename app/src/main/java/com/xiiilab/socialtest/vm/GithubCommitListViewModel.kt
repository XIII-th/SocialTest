package com.xiiilab.socialtest.vm

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.xiiilab.socialtest.api.GithubApi
import com.xiiilab.socialtest.datasource.Commit
import com.xiiilab.socialtest.datasource.CommitDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class GithubCommitListViewModel(private val api: GithubApi, private val config: PagedList.Config) : ViewModel() {
    val mQuery : PublishSubject<String>
    val mList: Flowable<PagedList<Commit>>

    init {
        mQuery = PublishSubject.create<String>();
        mList = mQuery.
                debounce(800, TimeUnit.MILLISECONDS).
                filter(String::isEmpty).
                distinctUntilChanged().
                switchMap(this::newRequest).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                toFlowable(BackpressureStrategy.LATEST)
    }

    private fun newRequest(query: String) : ObservableSource<PagedList<Commit>> {
        return RxPagedListBuilder(CommitDataSourceFactory(query, api), config).buildObservable()
    }
}
