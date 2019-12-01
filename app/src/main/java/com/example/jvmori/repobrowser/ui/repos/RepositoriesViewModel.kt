package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.data.repos.RepoResult
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository
) : ViewModel() {

    private val _results = MutableLiveData<Resource<PagedList<RepoEntity>>>()
    val results: LiveData<Resource<PagedList<RepoEntity>>> = _results

    private val publishSubject = PublishSubject.create<String>()
    private val disposable = CompositeDisposable()
    private val tetrisDisposable = CompositeDisposable()

    @Inject
    lateinit var networkDisposable: CompositeDisposable
    private lateinit var repoResult: RepoResult

    fun fetchRepos(query: String = "tetris") {
        repoResult = repository.fetchRepos(query)
        _results.value = Resource.loading(null)
        tetrisDisposable.add(
            repoResult.data?.subscribe(
                {
                    _results.value = Resource.success(it)
                }, {
                    _results.value = Resource.error(it.message ?: "", null)
                }
            )!!
        )
    }

    fun configurePublishSubject() {
        disposable.add(
            publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { query -> query.isNotEmpty() }
                .switchMap {
                    tetrisDisposable.clear()
                    repoResult = repository.fetchRepos(it)
                    checkNetworkStatus()
                    repoResult.data
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _results.value = Resource.success(result)
                }, {
                    _results.value = Resource.error(it.message ?: "", null)
                })
        )
    }

    private fun checkNetworkStatus() {
        disposable.add(
            repoResult.networkStatus.subscribe {
                when (it.status) {
                    Resource.Status.LOADING -> _results.postValue(Resource.loading(null))
                    Resource.Status.ERROR -> _results.postValue(Resource.error(it.data ?: "Network error", null))
                }
            }
        )
    }

    fun onQueryTextChange(query: String?) {
        if (query != null && query.isNotEmpty()) {
            publishSubject.onNext(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkDisposable.clear()
        disposable.clear()
    }
}