package com.example.jvmori.repobrowser.ui.repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.databinding.RepoItemBinding
import com.example.jvmori.repobrowser.ui.BaseAdapter

class ReposAdapter(
     items: List<ReposUI>
) : BaseAdapter<ReposUI>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ReposUI> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater, parent, false)
        return RepoViewHolder(binding)
    }

    class RepoViewHolder(private val binding: RepoItemBinding) :
        BaseAdapter.BaseViewHolder<ReposUI>(binding.root) {
        override fun bindView(item: ReposUI?) {
            with(binding) {
                repo = item
                executePendingBindings()
            }
        }
    }
}