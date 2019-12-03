package com.example.jvmori.repobrowser.ui.repos

import androidx.recyclerview.widget.RecyclerView
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.databinding.LoadingRepoItemBinding
import com.example.jvmori.repobrowser.databinding.RepoItemBinding
import com.example.jvmori.repobrowser.utils.NetworkState

class ReposViewHolder (private val binding : RepoItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindView(item: RepoEntity) {
        with (binding) {
            repo = item
            executePendingBindings()
        }
    }
}
class LoadingViewHolder(private val binding: LoadingRepoItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindView(item : NetworkState){
        with (binding){
            networkStatus = item
            executePendingBindings()
        }
    }
}

