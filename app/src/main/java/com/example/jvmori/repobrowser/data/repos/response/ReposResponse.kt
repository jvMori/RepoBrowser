package com.example.jvmori.repobrowser.data.repos.response


import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos_table", primaryKeys = ["repo_query", "repo_page"])
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