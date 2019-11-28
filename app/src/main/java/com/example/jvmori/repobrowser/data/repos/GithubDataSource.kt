package com.example.jvmori.repobrowser.data.repos

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.utils.TAG
import com.example.jvmori.repobrowser.utils.dataMapper
import io.reactivex.disposables.CompositeDisposable

class GithubDataSource(
    private val networkDataSource: ReposNetworkDataSource,
    private var query : String,
    private var disposable : CompositeDisposable
) : PageKeyedDataSource<Int, ReposUI>() {

    private val firstPage = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ReposUI>
    ) {
        disposable.add(
            networkDataSource.fetchRepos(
                query,
                params.requestedLoadSize,
                firstPage
            ).subscribe(
                {
                    val mappedData = dataMapper(it.repositories)
                    callback.onResult(mappedData, null, firstPage + 1)
                }, {
                    Log.e(TAG, "Failed to fetch data!")
                }
            )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ReposUI>) {
        disposable.add(
            networkDataSource.fetchRepos(
                query,
                params.requestedLoadSize,
                params.key
            ).subscribe(
                {
                    val totalPages = 30
                    val key = if (params.key < totalPages) params.key + 1 else null
                    val mappedData = dataMapper(it.repositories)
                    callback.onResult(mappedData, key)
                }, {
                    Log.e(TAG, "Failed to fetch data!")
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ReposUI>) {
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
                }, {
                    Log.e(TAG, "Failed to fetch data!")
                }
            )
        )
    }
}