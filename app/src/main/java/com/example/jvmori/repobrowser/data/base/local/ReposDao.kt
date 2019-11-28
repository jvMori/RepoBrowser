package com.example.jvmori.repobrowser.data.base.local

import androidx.room.*
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Repo>)

    @Query("Select * from repos_table where repo_name like:repoQuery")
    fun getRepos(repoQuery : String) : androidx.paging.DataSource.Factory<Int, Repo>

    @Query("Delete from repos_table where repo_name like:repoQuery")
    fun deleteRepos(repoQuery : String)

    @Transaction
    fun updateData(data : ReposResponse){
        deleteRepos(data.query)
        insert(data.repositories)
    }
}