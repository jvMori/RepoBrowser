package com.example.jvmori.repobrowser.data.repos

import android.util.Log
import com.example.jvmori.repobrowser.data.base.NetworkBoundResource
import com.example.jvmori.repobrowser.data.base.local.ReposDao
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import com.example.jvmori.repobrowser.utils.TAG
import com.example.jvmori.repobrowser.utils.minTime
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    private val networkDataSource: ReposNetworkDataSource,
    private val dao: ReposDao
) : ReposRepository, NetworkBoundResource<ReposResponse, String> {

    override fun fetchRepos(query: String): Flowable<ReposResponse> {
        return result(query)
    }

    override fun getLocal(params: String): Maybe<ReposResponse> {
        return dao.getRepos(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun getRemote(params: String): Observable<ReposResponse> {
        return networkDataSource.fetchRepos(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun saveCallResult(data: ReposResponse, params: String) {
        Completable.fromAction {
            data.timestamp = System.currentTimeMillis()
            data.query = params
            dao.updateData(data)
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnError { error -> onErrorOccur(error.message ?: "") }
            .subscribe()
    }

    override fun isUpToDate(data: ReposResponse): Boolean {
        val timeFromLastRefresh = System.currentTimeMillis() -  data.timestamp
        val isFirstTimeLoaded = timeFromLastRefresh == System.currentTimeMillis()
        return  isFirstTimeLoaded ||  timeFromLastRefresh < minTime
    }

    override fun isNotEmpty(data: ReposResponse): Boolean {
       return data.repositories.isNotEmpty()
    }

    override fun onErrorOccur(message: String) {
        Log.i(TAG, message)
    }
}