package com.example.jvmori.repobrowser.data.base.local

import androidx.room.*
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<RepoEntity>)

    @Query("Select * from repos_table where repo_query like:repoQuery")
    fun getRepos(repoQuery : String) : androidx.paging.DataSource.Factory<Int, RepoEntity>

    @Query("Delete from repos_table where repo_name like:repoQuery")
    fun deleteRepos(repoQuery : String)

}