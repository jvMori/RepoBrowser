package com.example.jvmori.repobrowser.data.repos

import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Observable

interface ReposNetworkDataSource {
    fun fetchRepos(query : String) : Observable<ReposResponse>
}