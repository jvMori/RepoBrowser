package com.example.jvmori.repobrowser.data.repos

import com.example.jvmori.repobrowser.data.base.network.GithubApi
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import retrofit2.Call
import javax.inject.Inject

class ReposNetworkDataSourceImpl
@Inject constructor(
    private val githubApi: GithubApi
) : ReposNetworkDataSource {

    override fun fetchRepos(
        query: String,
        loadSize: Int,
        page: Int
    ): Call<ReposResponse> {
        return githubApi.fetchRepos(query, loadSize, page)
    }
}