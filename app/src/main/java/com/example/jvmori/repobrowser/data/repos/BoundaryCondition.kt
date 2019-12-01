package com.example.jvmori.repobrowser.data.repos

import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.utils.PagingRequestHelper
import com.example.jvmori.repobrowser.utils.dataMapperRequestedReposToEntities
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class BoundaryCondition(
    private val query: String,
    private val networkDataSource: ReposNetworkDataSource,
    private val cache: LocalCache,
    private val disposable: CompositeDisposable,
    private val helper: PagingRequestHelper
) : PagedList.BoundaryCallback<RepoEntity>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveInit(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: RepoEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveAfter(query, itemAtEnd.currentPage + 1)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    private fun requestAndSaveInit(query: String) {
        fetchFromNetworkAndSave(query, 1, null)
    }

    private fun requestAndSaveAfter(query: String, page: Int) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
            fetchFromNetworkAndSave(query, page, helperCallback)
        }
    }

    private fun fetchFromNetworkAndSave(
        query: String,
        page: Int,
        helperCallback: PagingRequestHelper.Request.Callback?
    ) {
        disposable.add(
            networkDataSource.fetchRepos(query, NETWORK_PAGE_SIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        val data = it.repositories
                        cache.insert(dataMapperRequestedReposToEntities(data, query, page)) {
                            helperCallback?.recordSuccess()
                        }
                    }, {
                        helperCallback?.recordFailure(it)
                    }
                )
        )
    }
}