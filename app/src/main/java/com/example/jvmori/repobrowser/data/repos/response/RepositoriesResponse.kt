package com.example.jvmori.repobrowser.data.repos.response


import com.google.gson.annotations.SerializedName

data class RepositoriesResponse(
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean?,
    @SerializedName("items")
    var repositories: List<Repo?>?,
    @SerializedName("total_count")
    var totalCount: Int?
)