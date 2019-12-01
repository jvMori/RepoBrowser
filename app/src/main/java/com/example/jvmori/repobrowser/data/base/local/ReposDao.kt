package com.example.jvmori.repobrowser.data.base.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<RepoEntity>)

    @Query("Select * from repos_table where repo_query like:repoQuery ORDER BY current_page ASC")
    fun getRepos(repoQuery : String) : androidx.paging.DataSource.Factory<Int, RepoEntity>

    @Query("Delete from repos_table where repo_name like:repoQuery")
    fun deleteRepos(repoQuery : String)

}