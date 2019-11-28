package com.example.jvmori.repobrowser.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.example.jvmori.repobrowser.data.repos.ReposUI

class ReposDiffUtilCallback : DiffUtil.ItemCallback<ReposUI>() {
    override fun areItemsTheSame(oldItem: ReposUI, newItem: ReposUI): Boolean {
        return oldItem.nameOfRepo == newItem.nameOfRepo
    }

    override fun areContentsTheSame(oldItem: ReposUI, newItem: ReposUI): Boolean {
        return oldItem.nameOfRepo == newItem.nameOfRepo &&
                oldItem.ownerLoginName == newItem.ownerLoginName &&
                oldItem.sizeOfRepo == newItem.sizeOfRepo
    }
}