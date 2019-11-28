package com.example.jvmori.repobrowser.ui.repos

import androidx.recyclerview.widget.RecyclerView
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.databinding.RepoItemBinding
import com.example.jvmori.repobrowser.utils.mapRepo

class ReposViewHolder (private val binding : RepoItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindView(item: Repo?) {
        with(binding) {
            repo = mapRepo(item)
            executePendingBindings()
        }
    }
}