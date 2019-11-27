package com.example.jvmori.repobrowser.data.base.network

import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    fun fetchRepos(@Query("q") query : String) : Observable<ReposResponse>
}