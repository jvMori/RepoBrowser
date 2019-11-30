package com.example.jvmori.repobrowser.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.utils.PagingRequestHelper
import com.example.jvmori.repobrowser.utils.dataMapperRequestedReposToEntities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoundaryCondition(
    private val query: String,
    private val networkDataSource: ReposNetworkDataSource,
    private val cache: LocalCache,
    private val disposable: CompositeDisposable,
    private val helper: PagingRequestHelper
) : PagedList.BoundaryCallback<RepoEntity>() {

    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveData(query, 1, PagingRequestHelper.RequestType.INITIAL)
    }

    override fun onItemAtEndLoaded(itemAtEnd: RepoEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveData(query, itemAtEnd.currentPage + 1, PagingRequestHelper.RequestType.AFTER)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    private fun requestAndSaveData(query: String, page: Int, requestType: PagingRequestHelper.RequestType) {
        helper.runIfNotRunning(requestType) { helperCallback ->
            disposable.add(
                networkDataSource.fetchRepos(query, NETWORK_PAGE_SIZE, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            val data = it.repositories
                            cache.insert(dataMapperRequestedReposToEntities(data, query, page)) {
                                helperCallback.recordSuccess()
                            }
                        }, {
                            helperCallback.recordFailure(it)
                        }
                    )
            )
        }
    }
}