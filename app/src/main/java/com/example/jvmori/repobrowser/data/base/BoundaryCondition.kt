package com.example.jvmori.repobrowser.data.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.repos.ReposNetworkDataSource
import com.example.jvmori.repobrowser.data.repos.response.Repo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoundaryCondition(
    private val query: String,
    private val networkDataSource: ReposNetworkDataSource,
    private val cache: LocalCache,
    private val disposable: CompositeDisposable
) : PagedList.BoundaryCallback<Repo>() {

    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()

    val networkErrors: LiveData<String>
        get() = _networkErrors

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveData(query)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        disposable.add(
            networkDataSource.fetchRepos(query, NETWORK_PAGE_SIZE, lastRequestedPage)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        cache.insert(it.repositories){
                            lastRequestedPage++
                            isRequestInProgress = false
                        }
                    }, {
                        isRequestInProgress = false
                    }
                )
        )

    }
}