package com.example.jvmori.repobrowser.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoundaryCondition(
    private val query: String,
    private val networkDataSource: ReposNetworkDataSource,
    private val cache: LocalCache
) : PagedList.BoundaryCallback<Repo>() {

    private var lastRequestedPage = 1
    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveData(query)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    private fun requestAndSaveData(query: String) {

        if (isRequestInProgress) return

        isRequestInProgress = true
        networkDataSource.fetchRepos(query,
            NETWORK_PAGE_SIZE, lastRequestedPage)
            .enqueue(object : Callback<ReposResponse> {

                override fun onFailure(call: Call<ReposResponse>, t: Throwable) {
                    isRequestInProgress = false
                }

                override fun onResponse(
                    call: Call<ReposResponse>,
                    response: Response<ReposResponse>
                ) {
                   success(response)
                }
            })
    }
    private fun success(response : Response<ReposResponse>){
        val data = response.body()?.repositories ?: listOf()
        cache.insert(data) {
            lastRequestedPage++
            isRequestInProgress = false
        }
    }

}