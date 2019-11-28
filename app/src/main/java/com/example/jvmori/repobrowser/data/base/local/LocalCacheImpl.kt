package com.example.jvmori.repobrowser.data.base.local

import android.util.Log
import androidx.paging.DataSource
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.utils.TAG
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LocalCacheImpl @Inject constructor(
    private var dao: ReposDao
) : LocalCache {
    override fun insert(repos: List<Repo>, onSuccess: () -> Unit) {
        Completable.fromAction {
            dao.insert(repos)
        }.subscribeOn(Schedulers.io())
            .doOnError {
                Log.i(TAG, "Error while saving data")
            }
            .subscribe()
    }

    override fun getAllByName(query: String): DataSource.Factory<Int, Repo> {
        val queryClean = "%${query.replace(' ', '%')}%"
        return dao.getRepos(queryClean)
    }
}