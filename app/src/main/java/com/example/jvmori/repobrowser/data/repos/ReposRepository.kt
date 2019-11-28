package com.example.jvmori.repobrowser.data.repos

interface ReposRepository {
    fun fetchRepos(
        query: String = "tetris"
    ): RepoResult
}