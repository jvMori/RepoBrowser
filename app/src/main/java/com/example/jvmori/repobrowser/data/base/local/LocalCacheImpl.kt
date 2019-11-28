package com.example.jvmori.repobrowser.data.base.local

import androidx.paging.DataSource
import com.example.jvmori.repobrowser.data.repos.response.Repo
import io.reactivex.Completable
import javax.inject.Inject

class LocalCacheImpl @Inject constructor(
    var dao: ReposDao
) : LocalCache {
    override fun insert(repos: List<Repo>) {
        Completable.fromAction {
            dao.insert(repos)
        }.subscribe()
    }

    override fun getAllByName(query: String): DataSource.Factory<Int, Repo> {
        val queryClean = "%${query.replace(' ', '%')}%"
        return dao.getRepos(queryClean)
    }
}