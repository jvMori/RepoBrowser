package com.example.jvmori.repobrowser.data.base.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jvmori.repobrowser.data.repos.response.ReposResponse
import com.example.jvmori.repobrowser.utils.Converters

@Database(entities = [ReposResponse::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RepoBrowserDatabase : RoomDatabase(){
    abstract fun reposDao() : ReposDao
}
