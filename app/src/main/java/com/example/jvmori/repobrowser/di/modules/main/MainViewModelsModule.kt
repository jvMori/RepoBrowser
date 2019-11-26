package com.example.jvmori.repobrowser.di.modules.main

import androidx.lifecycle.ViewModel
import com.example.jvmori.repobrowser.di.scopes.MainActivityScope
import com.example.jvmori.repobrowser.di.viewmodel.ViewModelKey
import com.example.jvmori.repobrowser.ui.repos.RepositoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepositoriesViewModel::class)
    @MainActivityScope
    abstract fun bindRepositoriesViewModel(discoverViewModel: RepositoriesViewModel): ViewModel

}