package com.example.jvmori.repobrowser.data.repos.response


import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ReposResponse(
    @ColumnInfo(name = "repo_query")
    var query : String,
    @ColumnInfo(name = "repo_page")
    var page : Int,
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,
    @SerializedName("items")
    var repositories: List<Repo>,
    @SerializedName("total_count")
    var totalCount: Int,
    var timestamp : Long
)