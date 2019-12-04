package com.example.jvmori.repobrowser.ui.repos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.jvmori.repobrowser.R
import com.example.jvmori.repobrowser.data.base.local.RepoEntity
import com.example.jvmori.repobrowser.data.base.network.Resource
import com.example.jvmori.repobrowser.data.repos.RepoResult
import com.example.jvmori.repobrowser.data.repos.ReposRepository
import com.example.jvmori.repobrowser.ui.MainActivity
import com.example.jvmori.repobrowser.utils.NetworkState
import com.example.jvmori.repobrowser.utils.NetworkStatus
import com.example.jvmori.repobrowser.utils.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RepositoriesViewModel @Inject constructor(
    private val repository: ReposRepository,
    private val context : MainActivity
) : ViewModel() {

    private val _results = MutableLiveData<Resource<PagedList<RepoEntity>>>()
    val results: LiveData<Resource<PagedList<RepoEntity>>> = _results

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState> = _networkState

    private val publishSubject = PublishSubject.create<String>()
    private val disposable = CompositeDisposable()
    private val tetrisDisposable = CompositeDisposable()

    @Inject
    lateinit var networkDisposable: CompositeDisposable
    @Inject
    lateinit var networkStatus : PublishSubject<NetworkStatus>
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

    fun replay(){
        Log.i(TAG, "Cliecked")
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
                        is NetworkStatus.NetworkErrorForbidden -> {
                            _networkState.value = NetworkState(
                                true,
                                context.resources.getString(R.string.error_forbidden),
                                false
                            )
                        }
                        is NetworkStatus.NetworkErrorUnknown ->
                        {
                            _networkState.value = NetworkState(
                                true,
                                context.resources.getString(R.string.error_network_unknown),
                                false
                            )
                        }
                    }
                }
                .subscribe()
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
        networkStatus.onComplete()
    }
}