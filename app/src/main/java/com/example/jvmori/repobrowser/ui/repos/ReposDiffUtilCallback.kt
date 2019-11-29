package com.example.jvmori.repobrowser.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.repos.response.Repo

class ReposDiffUtilCallback : DiffUtil.ItemCallback<RepoEntity>() {
    override fun areItemsTheSame(oldItem: RepoEntity, newItem: RepoEntity): Boolean {
        return oldItem.nameOfRepo == newItem.nameOfRepo
    }

    override fun areContentsTheSame(oldItem: RepoEntity, newItem: RepoEntity): Boolean {
        return oldItem.nameOfRepo == newItem.nameOfRepo &&
                oldItem.ownerLoginName == newItem.ownerLoginName &&
                oldItem.sizeOfRepo == newItem.sizeOfRepo
    }
}