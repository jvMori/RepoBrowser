package com.example.jvmori.repobrowser.data.base.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "repos_table", primaryKeys = ["repo_id"])
data class RepoEntity (
    @ColumnInfo(name = "repo_id")
    var repoId : Int,
    @ColumnInfo(name = "repo_name")
    var nameOfRepo : String,
    @ColumnInfo(name = "owner_login")
    var ownerLoginName : String,
    @ColumnInfo(name = "repo_size")
    var sizeOfRepo : Int,
    @ColumnInfo(name = "repo_has_wiki")
    var hasWiki : Boolean,
    @ColumnInfo(name = "repo_query")
    var query : String,
    @ColumnInfo(name="current_page")
    var currentPage : Int
)
