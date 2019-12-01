package com.example.jvmori.repobrowser.data.repos

import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.utils.dataMapperRequestedReposToEntities
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoundaryCondition(
    private val query: String,
    private val networkDataSource: ReposNetworkDataSource,
    private val cache: LocalCache,
    private val disposable: CompositeDisposable
) : PagedList.BoundaryCallback<RepoEntity>() {

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        fetchFromNetworkAndSave(query, 1)
    }

    override fun onItemAtEndLoaded(itemAtEnd: RepoEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
        fetchFromNetworkAndSave(query, itemAtEnd.currentPage + 1)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    var networkStatus : Observable<Resource<String>> = Observable.just(Resource.loading(""))

    private fun fetchFromNetworkAndSave(
        query: String,
        page: Int
    ) {
        disposable.add(
            networkDataSource.fetchRepos(query, NETWORK_PAGE_SIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        val data = it.repositories
                        cache.insert(dataMapperRequestedReposToEntities(data, query, page)) {
                            networkStatus = Observable.just(Resource.success("success"))
                        }
                    }, {
                        networkStatus = Observable.just(Resource.error(it.message ?: "Network error", ""))
                    }
                )
        )
    }
}