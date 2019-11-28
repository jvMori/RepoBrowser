package com.example.jvmori.repobrowser.data.repos.response


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos_table", primaryKeys = ["repo_id"])
data class Repo(
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("default_branch")
    var defaultBranch: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("fork")
    var fork: Boolean,
    @SerializedName("forks_count")
    var forksCount: Int,
    @SerializedName("full_name")
    var fullName: String,
    @SerializedName("html_url")
    var htmlUrl: String,
    @SerializedName("id")
    @ColumnInfo(name = "repo_id")
    var id: Int,
    @SerializedName("language")
    var language: String,
    @SerializedName("name")
    @ColumnInfo(name = "repo_name")
    var name: String,
    @SerializedName("open_issues_count")
    var openIssuesCount: Int,
    @SerializedName("owner")
    @Embedded(prefix = "owner_")
    var owner: Owner,
    @SerializedName("pushed_at")
    var pushedAt: String,
    @SerializedName("score")
    var score: Double,
    @SerializedName("size")
    var size: Int,
    @SerializedName("stargazers_count")
    var stargazersCount: Int,
    @SerializedName("updated_at")
    var updatedAt: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("watchers_count")
    var watchersCount: Int,
    @SerializedName("has_wiki")
    var has_wiki: Boolean
)