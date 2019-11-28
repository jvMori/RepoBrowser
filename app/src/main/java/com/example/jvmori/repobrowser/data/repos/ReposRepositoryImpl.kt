package com.example.jvmori.repobrowser.data.repos

import androidx.paging.LivePagedListBuilder
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.utils.DATABASE_PAGE_SIZE
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    private val networkDataSource: ReposNetworkDataSource,
    private val localCache: LocalCache
) : ReposRepository {
    override fun fetchRepos(
        query: String
    ): RepoResult {
        val dataSourceFactory = localCache.getAllByName(query)

        val boundaryCallback = BoundaryCondition(
            query,
            networkDataSource,
            localCache
        )
        val networkErrors = boundaryCallback.networkErrors

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return RepoResult(data, networkErrors)
    }
}