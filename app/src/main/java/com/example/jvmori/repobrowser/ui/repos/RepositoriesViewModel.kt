package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.repos.GithubDataSource
import com.example.jvmori.repobrowser.data.repos.ReposNetworkDataSource
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.data.repos.Status
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class RepositoriesViewModel @Inject constructor(
    private val networkDataSource: ReposNetworkDataSource
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _status : MutableLiveData<Status> = MutableLiveData()
    val status : LiveData<Status> = _status

    private val config = PagedList.Config.Builder()
        .setPageSize(10)
        .setEnablePlaceholders(false)
        .build()

    fun fetchReposLiveData(query: String) =
         initializedPagedListBuilder(config, query).build()


    private fun initializedPagedListBuilder(
        config: PagedList.Config,
        query: String
    ): LivePagedListBuilder<Int, ReposUI> {
        val dataSourceFactory = object : DataSource.Factory<Int, ReposUI>() {
            override fun create(): DataSource<Int, ReposUI> {
                return GithubDataSource(networkDataSource, query, disposable, _status)
            }
        }
        return LivePagedListBuilder<Int, ReposUI>(dataSourceFactory, config)
    }

    private val filterTextAll = MutableLiveData<String>()
    fun getSearchResults() : LiveData<PagedList<ReposUI>> {
        return Transformations
            .switchMap(filterTextAll) { input ->
                return@switchMap fetchReposLiveData(input)
            }
    }
    fun onQueryTextChange(query: String?, lifecycleOwner: LifecycleOwner) {
        if (query != null && query.isNotEmpty()){
            filterTextAll.removeObservers(lifecycleOwner)
            filterTextAll.value = query
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}