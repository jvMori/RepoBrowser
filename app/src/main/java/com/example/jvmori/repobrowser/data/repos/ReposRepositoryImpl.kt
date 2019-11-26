package com.example.jvmori.repobrowser.data.repos

import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Observable
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor (
    val networkDataSource: ReposNetworkDataSource
) : ReposRepository {
    override fun fetchRepos(query: String): Observable<ReposResponse> {
        return networkDataSource.fetchRepos(query)
    }
}