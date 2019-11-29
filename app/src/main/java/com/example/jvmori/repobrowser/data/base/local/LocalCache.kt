package com.example.jvmori.repobrowser.data.base.local

import androidx.paging.DataSource

interface LocalCache {
    fun insert(repos : List<RepoEntity>, onSuccess : () -> Unit)
    fun getAllByName(query : String) : DataSource.Factory<Int, RepoEntity>
}