package com.example.jvmori.repobrowser.data.repos

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.utils.DATABASE_PAGE_SIZE
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    private val networkDataSource: ReposNetworkDataSource,
    private val localCache: LocalCache,
    private val disposable: CompositeDisposable
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

        val networkErrors = boundaryCallback.networkErrors

        val config = PagedList.Config.Builder()
            .setPageSize(DATABASE_PAGE_SIZE)
            .setPrefetchDistance(0)
            .setInitialLoadSizeHint(DATABASE_PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()

        val data = RxPagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()

        return RepoResult(data, networkErrors)
    }
}