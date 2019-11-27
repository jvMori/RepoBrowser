package com.example.jvmori.repobrowser.di.modules.main

import com.example.jvmori.repobrowser.data.base.local.ReposDao
import com.example.jvmori.repobrowser.data.base.network.GithubApi
import com.example.jvmori.repobrowser.data.repos.ReposNetworkDataSource
import com.example.jvmori.repobrowser.data.repos.ReposNetworkDataSourceImpl
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.data.repos.ReposRepositoryImpl
import com.example.jvmori.repobrowser.di.scopes.MainActivityScope
import dagger.Module
import dagger.Provides

@Module
class ReposModule {

    @MainActivityScope
    @Provides
    fun provideReposNetworkDataSource(githubApi: GithubApi): ReposNetworkDataSource =
        ReposNetworkDataSourceImpl(githubApi)

    @MainActivityScope
    @Provides
    fun provideReposRepository(reposNetworkDataSource: ReposNetworkDataSource, dao: ReposDao) : ReposRepository =
        ReposRepositoryImpl(reposNetworkDataSource, dao)
}