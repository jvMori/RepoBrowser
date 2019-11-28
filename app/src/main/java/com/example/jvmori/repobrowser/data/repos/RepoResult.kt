package com.example.jvmori.repobrowser.data.repos

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.repos.response.Repo

data class RepoResult(
    val data: LiveData<PagedList<Repo>>,
    val networkErrors: LiveData<String>
)