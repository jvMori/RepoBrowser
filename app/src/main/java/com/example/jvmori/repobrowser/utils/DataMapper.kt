package com.example.jvmori.repobrowser.utils

import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.data.repos.response.Repo

fun dataMapper(requestData: List<Repo>): List<ReposUI> {
    val array = mutableListOf<ReposUI>()
    requestData.forEach { repo ->
        array.add(
            ReposUI(
                repo.name,
                repo.owner.login,
                repo.size,
                repo.has_wiki
            )
        )
    }
    return array
}