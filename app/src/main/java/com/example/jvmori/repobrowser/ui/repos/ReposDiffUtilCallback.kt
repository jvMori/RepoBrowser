package com.example.jvmori.repobrowser.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.example.jvmori.repobrowser.data.repos.response.Repo

class ReposDiffUtilCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.owner == newItem.owner &&
                oldItem.size == newItem.size
    }
}