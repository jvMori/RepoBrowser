package com.example.jvmori.repobrowser.data.repos.response


import com.google.gson.annotations.SerializedName

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
    var id: Int,

    @SerializedName("language")
    var language: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("open_issues_count")
    var openIssuesCount: Int,

    @SerializedName("owner")
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