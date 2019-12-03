package com.example.jvmori.repobrowser.utils

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

sealed class NetworkStatus {
    object NetworkLoading : NetworkStatus()
    object NetworkSuccess : NetworkStatus()
    object NetworkErrorForbidden : NetworkStatus()
    object NetworkErrorUnknown : NetworkStatus()
}

fun handleNetworkError(status : Throwable?) : NetworkStatus {
    if (status is HttpException){
        when (status.code()) {
            403 -> NetworkStatus.NetworkErrorForbidden
        }
    }
    return NetworkStatus.NetworkErrorUnknown
}

data class NetworkState (
    var errorMessage : String,
    var isLoading : Boolean
)