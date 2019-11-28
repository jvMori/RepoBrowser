package com.example.jvmori.repobrowser.data.repos

import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import retrofit2.Call

interface ReposNetworkDataSource {
    fun fetchRepos(
        query: String = "tetris",
        loadSize: Int = 10,
        page: Int = 1
    ): Call<ReposResponse>
}