package com.example.jvmori.repobrowser.data.repos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.jvmori.repobrowser.utils.TAG
import com.example.jvmori.repobrowser.utils.dataMapper
import io.reactivex.disposables.CompositeDisposable

sealed class Status {
    object SUCCESS : Status()
    object ERROR : Status()
    object LOADING : Status()
}
class GithubDataSource(
    private val networkDataSource: ReposNetworkDataSource,
    private var query : String,
    private var disposable : CompositeDisposable,
    private val status : MutableLiveData<Status>
) : PageKeyedDataSource<Int, ReposUI>() {

    private val firstPage = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ReposUI>
    ) {
        status.postValue(Status.LOADING)
        disposable.add(
            networkDataSource.fetchRepos(
                query,
                params.requestedLoadSize,
                firstPage
            ).subscribe(
                {
                    val mappedData = dataMapper(it.repositories)
                    callback.onResult(mappedData, null, firstPage + 1)
                    status.postValue(Status.SUCCESS)
                }, {
                    error()
                }
            )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ReposUI>) {
        status.postValue(Status.LOADING)
        disposable.add(
            networkDataSource.fetchRepos(
                query,
                params.requestedLoadSize,
                params.key
            ).subscribe(
                {
                    val totalPages = (it.totalCount / params.requestedLoadSize) - (it.totalCount % params.requestedLoadSize)
                    val key = if (params.key < totalPages) params.key + 1 else null
                    val mappedData = dataMapper(it.repositories)
                    callback.onResult(mappedData, key)
                    status.postValue(Status.SUCCESS)
                }, {
                    error()
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ReposUI>) {
        status.postValue(Status.LOADING)
        disposable.add(
            networkDataSource.fetchRepos(
                query,
                params.requestedLoadSize,
                params.key
            ).subscribe(
                {
                    val key = if (params.key > 0) params.key - 1 else null
                    val mappedData = dataMapper(it.repositories)
                    callback.onResult(mappedData, key)
                    status.postValue(Status.SUCCESS)
                }, {
                    error()
                }
            )
        )
    }

    private fun error() {
        Log.e(TAG, "Failed to fetch data!")
        status.postValue(Status.ERROR)
    }
}