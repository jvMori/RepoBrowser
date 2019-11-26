package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jvmori.repobrowser.data.base.Resource
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.data.repos.response.Repo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository
) : ViewModel() {

    private val _repos: MutableLiveData<Resource<List<ReposUI>>> = MutableLiveData()
    val repos: LiveData<Resource<List<ReposUI>>> = _repos

    private val disposable = CompositeDisposable()

    fun fetchRepos(query: String) {
        _repos.value = Resource.loading(null)
        disposable.add(
            repository.fetchRepos(query)
                .flatMap {
                    return@flatMap Observable.just(
                        dataMapper(it.repositories)
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { success ->
                        _repos.value = Resource.success(success)
                    }, {
                        _repos.value = Resource.error(it.message ?: "", null)
                    }
                )
        )
    }

    private fun dataMapper(requestData: List<Repo>): List<ReposUI> {
        val array = mutableListOf<ReposUI>()
        requestData.forEach { repo ->
            array.add(
                ReposUI(
                    repo.name,
                    repo.owner.login,
                    repo.size,
                    repo.has_wiki
                )
            )
        }
        return array
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}