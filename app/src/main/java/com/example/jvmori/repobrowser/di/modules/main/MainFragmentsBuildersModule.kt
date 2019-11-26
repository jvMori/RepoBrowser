package com.example.jvmori.repobrowser.di.modules.main

import com.example.jvmori.repobrowser.ui.repos.RepositoriesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsBuildersModule {

    @ContributesAndroidInjector
    abstract fun contibuteRepositoriesFragment(): RepositoriesFragment
}