package com.example.jvmori.repobrowser.ui.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.databinding.LoadingItem
import com.example.jvmori.repobrowser.databinding.RepoItemBinding
import com.example.jvmori.repobrowser.utils.NetworkState

class ReposAdapter: PagedListAdapter<RepoEntity, RecyclerView.ViewHolder>(ReposDiffUtilCallback()) {

    private var isLoadingAdded = false

    object ViewType {
        const val ITEM = 0
        const val LOADING = 1
    }

    var networkState = NetworkState(false,"", false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.LOADING -> loadingViewHolder(parent)
        }
        return reposViewHolder(parent)
    }

    private fun reposViewHolder(parent: ViewGroup): ReposViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepoItemBinding.inflate(inflater, parent, false)
        return ReposViewHolder(binding)
    }

    private fun loadingViewHolder(parent: ViewGroup): LoadingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoadingItem.inflate(inflater, parent, false)
        return LoadingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.LOADING -> bindLoading(holder)
            ViewType.ITEM -> bindItem(holder, position)
        }
    }

    private fun bindLoading(
        holder: RecyclerView.ViewHolder
    ) {
        if (holder is LoadingViewHolder)
            holder.bindView(networkState)
    }

    private fun bindItem(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is ReposViewHolder)
            holder.bindView(
                getItem(position) ?: RepoEntity(0, "", "", 0, false, "", 1)
            )
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList?.size ?: 0 - 1 && isLoadingAdded) ViewType.LOADING else ViewType.ITEM
    }

    fun addLoadingAtBottom() {
        if (!isLoadingAdded) {
            isLoadingAdded = true
            currentList?.add(RepoEntity())
        }
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = currentList?.size ?: 0 - 1
        if (position != -1) {
            currentList?.removeAt(position)
        }
    }
}

