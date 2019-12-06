package com.example.jvmori.repobrowser.ui.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.utils.NetworkState
import com.example.jvmori.repobrowser.utils.NetworkStatus
import com.example.jvmori.repobrowser.utils.TAG
import io.reactivex.Observer
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

    private val _repositories = MutableLiveData<Resource<PagedList<RepoEntity>>>()
    val repositories: LiveData<Resource<PagedList<RepoEntity>>> = _repositories

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState> = _networkState

    private val source = PublishSubject.create<String>()

    @Inject
    lateinit var disposable: CompositeDisposable
    @Inject
    lateinit var networkStatus : PublishSubject<NetworkStatus>

    fun observeRepositoriesSource() {
        source
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { query -> query.isNotEmpty() }
            .switchMap {
                repository.fetchRepos(it).data
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver())
    }

    private fun getObserver() : Observer<PagedList<RepoEntity>>{
        return object : Observer<PagedList<RepoEntity>>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
               _repositories.value = Resource.loading(null)
            }

            override fun onNext(t: PagedList<RepoEntity>) {
                _networkState.value = NetworkState(false, "", false)
                _repositories.value = Resource.success(t)
            }

            override fun onError(e: Throwable) {
                _repositories.value = Resource.error(e.message ?: "", null)
            }
        }
    }

    fun replay(){
        Log.i(TAG, "Cliecked")
    }

    fun observeNetworkStatus() {
        disposable.add(
            networkStatus
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    when (it){
                        is NetworkStatus.NetworkLoading -> {
                            _networkState.value = NetworkState(false, "", true)
                        }
                        is NetworkStatus.NetworkSuccess -> {
                            _networkState.value = NetworkState(false, "", false)
                        }
                        is NetworkStatus.NetworkErrorUnknown ->
                        {
                            _networkState.value = NetworkState(
                                true,
                                "Could not fetch data due to network error.",
                                false
                            )
                        }
                    }
                }
                .subscribe()
        )
    }

    fun requestQuery(query: String?) {
        if (query != null && query.isNotEmpty()) {
            source.onNext(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        networkStatus.onComplete()
    }
}