package com.example.jvmori.repobrowser.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import com.example.jvmori.repobrowser.utils.dataMapperRequestedReposToEntities
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BoundaryCondition(
    private val query: String,
    private val networkDataSource: ReposNetworkDataSource,
    private val cache: LocalCache,
    private val disposable: CompositeDisposable
) : PagedList.BoundaryCallback<RepoEntity>() {

    private var lastRequestedPage = 1
    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: RepoEntity) {
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
            networkDataSource.fetchRepos(query,
                NETWORK_PAGE_SIZE, lastRequestedPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        success(it, query)
                    },{
                        isRequestInProgress = false
                        error(it.message ?: "Error while downloading data")
                    }
                )
        )
    }
    private fun success(response : ReposResponse, query: String){
        val data = response.repositories
        cache.insert(dataMapperRequestedReposToEntities(data, query)) {
            lastRequestedPage++
            isRequestInProgress = false
        }
    }

}