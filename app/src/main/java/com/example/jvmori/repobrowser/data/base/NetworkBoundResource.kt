package com.example.jvmori.repobrowser.data.base

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable

interface NetworkBoundResource<ResponseType, Params> {

    fun result(params: Params): Flowable<ResponseType> = Maybe.concat(getLocal(params), fetchFromNetwork(params))
        .filter { data ->  isNotEmpty(data) && isUpToDate(data)}
        .take(1)
        .doOnError { error -> onErrorOccur(error.message ?: "") }


    fun getLocal(params : Params): Maybe<ResponseType>
    fun getRemote(params : Params): Observable<ResponseType>
    fun saveCallResult(data: ResponseType, params: Params)
    fun isUpToDate(data: ResponseType): Boolean
    fun isNotEmpty(data : ResponseType) : Boolean
    fun onErrorOccur(message : String)

    private fun fetchFromNetwork(params: Params): Maybe<ResponseType> {
        return getRemote(params)
            .doOnNext {
                saveCallResult(it, params)
            }.firstElement()
    }
}