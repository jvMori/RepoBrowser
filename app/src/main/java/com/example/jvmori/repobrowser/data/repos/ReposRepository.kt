package com.example.jvmori.repobrowser.data.repos

import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Observable

interface ReposRepository {
    fun fetchRepos(query : String) : Observable<ReposResponse>
}