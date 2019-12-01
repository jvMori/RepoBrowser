package com.example.jvmori.repobrowser.data.base.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RepoEntity::class], version = 11, exportSchema = false)
abstract class RepoBrowserDatabase : RoomDatabase(){
    abstract fun reposDao() : ReposDao
}
