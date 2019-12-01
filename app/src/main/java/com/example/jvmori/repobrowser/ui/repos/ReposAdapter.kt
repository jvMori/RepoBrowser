package com.example.jvmori.repobrowser.ui.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.databinding.RepoItemBinding

class ReposAdapter : PagedListAdapter<RepoEntity, ReposViewHolder>(ReposDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater, parent, false)
        return ReposViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
       holder.bindView(getItem(position) ?: RepoEntity(0,"", "", 0, false, "", 1))
    }
}

