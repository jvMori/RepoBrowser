package com.example.jvmori.repobrowser.ui.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.databinding.RepoItemBinding

class ReposAdapter : PagedListAdapter<ReposUI, ReposViewHolder>(ReposDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater, parent, false)
        return ReposViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
       holder.bindView(getItem(position))
    }
}

