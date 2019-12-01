package com.example.jvmori.repobrowser.data.repos

import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.utils.PagingRequestHelper
import io.reactivex.Observable

data class RepoResult(
    var data: Observable<PagedList<RepoEntity>>?,
    var helper: PagingRequestHelper
)