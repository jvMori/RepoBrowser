package com.example.jvmori.repobrowser.data.base.local

import androidx.room.*
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Maybe

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data : ReposResponse)

    @Query("Select * from repos_table where repo_query like:repoQuery and repo_page like:repoPage ")
    fun getRepos(repoQuery : String, repoPage : Int = 0) : Maybe<ReposResponse>

    @Query("Delete from repos_table where repo_query like:repoQuery and repo_page like:repoPage ")
    fun deleteRepos(repoQuery : String, repoPage : Int = 0)

    @Transaction
    fun updateData(data : ReposResponse){
        deleteRepos(data.query, data.page)
        insert(data)
    }
}