package com.example.jvmori.repobrowser.data.base.local

import androidx.room.*
import com.example.jvmori.repobrowser.data.repos.response.Repo
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Maybe
import javax.sql.DataSource

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Repo>)

    @Query("Select * from repos_table where repo_query like:repoQuery")
    fun getRepos(repoQuery : String) : androidx.paging.DataSource.Factory<Int, Repo>

    @Query("Delete from repos_table where repo_query like:repoQuery and repo_page like:repoPage ")
    fun deleteRepos(repoQuery : String, repoPage : Int)

    @Transaction
    fun updateData(data : ReposResponse){
        deleteRepos(data.query, data.page)
        insert(data.repositories)
    }
}