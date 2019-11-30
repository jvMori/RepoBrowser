package com.example.jvmori.repobrowser.di.modules.main

import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.LocalCache
import com.example.jvmori.repobrowser.data.base.network.GithubApi
import com.example.jvmori.repobrowser.data.repos.ReposNetworkDataSource
import com.example.jvmori.repobrowser.data.repos.ReposNetworkDataSourceImpl
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.data.repos.ReposRepositoryImpl
import com.example.jvmori.repobrowser.di.scopes.MainActivityScope
import com.example.jvmori.repobrowser.utils.DATABASE_PAGE_SIZE
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ReposModule {

    @MainActivityScope
    @Provides
    fun provideDisposable(): CompositeDisposable = CompositeDisposable()

    @MainActivityScope
    @Provides
    fun providePagedListConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(DATABASE_PAGE_SIZE)
            .setPrefetchDistance(0)
            .setInitialLoadSizeHint(DATABASE_PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
    }

    @MainActivityScope
    @Provides
    fun provideReposNetworkDataSource(githubApi: GithubApi): ReposNetworkDataSource =
        ReposNetworkDataSourceImpl(githubApi)

    @MainActivityScope
    @Provides
    fun provideReposRepository(
        reposNetworkDataSource: ReposNetworkDataSource,
        localCache: LocalCache,
        disposable: CompositeDisposable,
        config: PagedList.Config
    ): ReposRepository =
        ReposRepositoryImpl(reposNetworkDataSource, localCache, disposable, config)
}