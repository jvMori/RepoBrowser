package com.example.jvmori.repobrowser.data.base

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [], version = 1, exportSchema = false)
abstract class RepoBrowserDatabase : RoomDatabase(){}
