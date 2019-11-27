package com.example.jvmori.repobrowser.di.modules.app

import android.app.Application
import androidx.room.Room
import com.example.jvmori.repobrowser.data.base.RepoBrowserDatabase
import com.example.jvmori.repobrowser.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @ApplicationScope
    fun provideDatabase(context: Application): RepoBrowserDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RepoBrowserDatabase::class.java,
            "repoBrowser.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

}