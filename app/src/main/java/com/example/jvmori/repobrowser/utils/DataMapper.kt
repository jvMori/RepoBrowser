package com.example.jvmori.repobrowser.utils

import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.repos.response.Repo

fun dataMapperRequestedReposToEntities(requestData: List<Repo>, query: String, page : Int): List<RepoEntity> {
    val array = mutableListOf<RepoEntity>()
    requestData.forEach { repo ->
        array.add(
            requestedRepoToEntity(repo, query, page)
        )
    }
    return array
}

fun requestedRepoToEntity(repo : Repo, query : String, page : Int) : RepoEntity {
    return RepoEntity(
        repo.id,
        repo.name,
        repo.owner.login,
        repo.size,
        repo.has_wiki,
        query,
        page
    )
}
