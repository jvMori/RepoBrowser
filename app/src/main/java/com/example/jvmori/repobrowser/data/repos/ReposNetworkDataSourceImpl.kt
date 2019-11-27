package com.example.jvmori.repobrowser.data.repos

import com.example.jvmori.repobrowser.data.base.network.GithubApi
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Observable
import javax.inject.Inject

class ReposNetworkDataSourceImpl
@Inject constructor(
    val githubApi: GithubApi
) : ReposNetworkDataSource {

    override fun fetchRepos(query: String): Observable<ReposResponse> {
        return githubApi.fetchRepos(query)
    }
}