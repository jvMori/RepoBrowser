package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository
) : ViewModel() {

    private val _results = MutableLiveData<Resource<PagedList<RepoEntity>>>()
    val results : LiveData<Resource<PagedList<RepoEntity>>> = _results

    private val publishSubject = PublishSubject.create<String>()
    private val disposable = CompositeDisposable()
    private val tetrisDisposable = CompositeDisposable()

    fun fetchRepos(query : String = "tetris"){
        _results.value = Resource.loading(null)
        tetrisDisposable.add(
            repository.fetchRepos(query).data?.subscribe(
                {
                    _results.value = Resource.success(it)
                },{
                    _results.value = Resource.error(it.message ?: "", null)
                }
            )!!
        )
    }

    fun configurePublishSubject() {
        disposable.add(
            publishSubject
                .debounce(600, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter{query -> query.isNotEmpty()}
                .switchMap {
                    _results.postValue(Resource.loading(null))
                    tetrisDisposable.clear()
                    repository.fetchRepos(it).data
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _results.value = Resource.success(result)
                }, {
                    _results.value = Resource.error(it.message ?: "", null)
                } )
        )
    }

    fun onQueryTextChange(query: String?) {
        if (query != null && query.isNotEmpty()){
            publishSubject.onNext(query)
        }
    }

    fun onQuerySubmit(){
        publishSubject.onComplete()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}