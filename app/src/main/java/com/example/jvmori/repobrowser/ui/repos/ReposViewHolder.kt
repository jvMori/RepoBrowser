package com.example.jvmori.repobrowser.ui.repos

import androidx.recyclerview.widget.RecyclerView
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.databinding.RepoItemBinding

class ReposViewHolder (private val binding : RepoItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindView(item: ReposUI?) {
        with(binding) {
            repo = item
            executePendingBindings()
        }
    }
}