package com.example.jvmori.repobrowser.data.base.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import io.reactivex.Maybe

@Dao
interface ReposDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data : ReposResponse)

    @Query("Select * from repos_table where repo_query like:repoQuery and repo_page like:repoPage ")
    fun getRepos(repoQuery : String, repoPage : Int = 0) : Maybe<ReposResponse>
}