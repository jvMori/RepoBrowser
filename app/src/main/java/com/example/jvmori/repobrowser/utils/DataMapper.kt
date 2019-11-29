package com.example.jvmori.repobrowser.utils

import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.data.repos.response.Repo

fun dataMapper(requestData: List<Repo>): List<ReposUI> {
    val array = mutableListOf<ReposUI>()
    requestData.forEach { repo ->
        array.add(
            mapRepo(repo)
        )
    }
    return array
}

fun dataMapperRequestedReposToEntities(requestData: List<Repo>, query: String): List<RepoEntity> {
    val array = mutableListOf<RepoEntity>()
    requestData.forEach { repo ->
        array.add(
            requestedRepoToEntity(repo, query)
        )
    }
    return array
}

fun requestedRepoToEntity(repo : Repo, query : String) : RepoEntity {
    return RepoEntity(
        repo.id,
        repo.name,
        repo.owner.login,
        repo.size,
        repo.has_wiki,
        query
    )
}

fun mapRepo(repo: Repo?): ReposUI {
    return ReposUI(
        repo?.name,
        repo?.owner?.login,
        repo?.size,
        repo?.has_wiki
    )

}