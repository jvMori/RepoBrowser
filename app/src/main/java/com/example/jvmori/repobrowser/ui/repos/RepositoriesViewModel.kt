package com.example.jvmori.repobrowser.ui.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.data.repos.ReposUI
import com.example.jvmori.repobrowser.data.repos.response.Repo
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository
) : ViewModel() {

    private val _repos: MutableLiveData<Resource<List<ReposUI>>> = MutableLiveData()
    val repos: LiveData<Resource<List<ReposUI>>> = _repos

    private val publishSubject = PublishSubject.create<String>()
    private val disposable = CompositeDisposable()

    fun configurePublishSubject() {
        disposable.add(
            publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter{query -> query.isNotEmpty()}
                .switchMap {
                    _repos.postValue(Resource.loading(null))
                    getRepos(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _repos.value = Resource.success(result)
                }, {
                    _repos.value = Resource.error(it.message ?: "", null)
                } )
        )
    }

   fun onQueryTextChange(query : String){
       publishSubject
           .onNext(query)
   }

    private fun getRepos(query: String): Observable<List<ReposUI>> {
        return repository.fetchRepos(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                return@flatMap Flowable.just(
                    dataMapper(it.repositories)
                )
            }
            .toObservable()
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