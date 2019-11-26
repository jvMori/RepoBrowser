package com.example.jvmori.repobrowser.data

import com.example.jvmori.repobrowser.data.repos.response.RepositoriesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/repositories")
    fun fetchRepos(@Query("q") query : String) : Observable<RepositoriesResponse>
}