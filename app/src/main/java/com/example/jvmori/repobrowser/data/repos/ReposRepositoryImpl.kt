package com.example.jvmori.repobrowser.data.repos

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    private val networkDataSource: ReposNetworkDataSource,
    private val localCache: LocalCache,
    private val disposable: CompositeDisposable,
    private val config: PagedList.Config
) : ReposRepository {

    override fun fetchRepos(
        query: String
    ): RepoResult {

        val dataSourceFactory = localCache.getAllByName(query)

        val boundaryCallback = BoundaryCondition(
            query,
            networkDataSource,
            localCache,
            disposable
        )

        val networkStatus = boundaryCallback.networkStatus

        val data = RxPagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()

        return RepoResult(data, networkStatus)
    }
}