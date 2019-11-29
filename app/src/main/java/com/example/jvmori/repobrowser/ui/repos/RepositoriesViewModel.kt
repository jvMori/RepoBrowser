package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.data.repos.RepoResult
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.data.repos.response.Repo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository
) : ViewModel() {

    private val _results = MutableLiveData<Resource<PagedList<RepoEntity>>>()
    val results : LiveData<Resource<PagedList<RepoEntity>>> = _results

    private val disposable = CompositeDisposable()

    fun fetchTetrisRepos(){
        _results.value = Resource.loading(null)
        disposable.add(
            repository.fetchRepos("tetris").data.subscribe(
                {
                    _results.value = Resource.success(it)
                },{
                    _results.value = Resource.error(it.message ?: "", null)
                }
            )
        )
    }


    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: LiveData<RepoResult> = Transformations.map(queryLiveData) {
        repository.fetchRepos(it)
    }

    val repos: Observable<PagedList<RepoEntity>> = repository.fetchRepos().data

    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) {
        it.networkErrors
    }

    fun onQueryTextChange(query: String?) {
        if (query != null && query.isNotEmpty()){
            queryLiveData.postValue(query)
        }
    }

    fun displayTetrisRepos(){
        queryLiveData.postValue("Tetris")
    }

}