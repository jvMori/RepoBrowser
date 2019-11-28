package com.example.jvmori.repobrowser.data.base.local

import androidx.paging.DataSource
import com.example.jvmori.repobrowser.data.repos.response.Repo

interface LocalCache {
    fun insert(repos : List<Repo>, onSuccess : () -> Unit)
    fun getAllByName(query : String) : DataSource.Factory<Int, Repo>
}