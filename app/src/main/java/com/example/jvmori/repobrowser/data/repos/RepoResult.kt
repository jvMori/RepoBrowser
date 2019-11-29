package com.example.jvmori.repobrowser.data.repos

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import io.reactivex.Observable

data class RepoResult(
    val data: Observable<PagedList<RepoEntity>>,
    val networkErrors: LiveData<String>
)