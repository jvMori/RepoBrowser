package com.example.jvmori.repobrowser.di.modules.app

import com.example.jvmori.repobrowser.di.modules.main.MainFragmentsBuildersModule
import com.example.jvmori.repobrowser.di.modules.main.ReposModule
import com.example.jvmori.repobrowser.di.scopes.MainActivityScope
import com.example.jvmori.repobrowser.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentsBuildersModule::class,
            ReposModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}