package com.example.jvmori.repobrowser.data.base.network

import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GithubApi {

    @GET("search/repositories")
    fun fetchRepos(
        @Query("q") query : String = "tetris",
        @Query("per_page") loadSize: Int = 10,
        @Query("page") page: Int = 1
    ) : Observable<ReposResponse>
}