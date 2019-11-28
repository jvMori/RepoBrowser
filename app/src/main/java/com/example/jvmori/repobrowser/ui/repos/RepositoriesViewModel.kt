package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.repos.RepoResult
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.data.repos.response.Repo
import javax.inject.Inject


class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository
) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val repoResult: LiveData<RepoResult> = Transformations.map(queryLiveData) {
        repository.fetchRepos(it)
    }

    val repos: LiveData<PagedList<Repo>> =
        Transformations.switchMap(repoResult) { it.data }

    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) {
        it.networkErrors
    }

    fun onQueryTextChange(query: String?) {
        if (query != null && query.isNotEmpty()){
            queryLiveData.postValue(query)
        }
    }

    fun displayTetrisRepos(){
        queryLiveData.postValue("tetris")
    }

}