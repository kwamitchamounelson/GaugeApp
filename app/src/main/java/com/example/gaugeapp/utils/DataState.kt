
package com.example.gaugeapp.utils

sealed class DataState<out T> {

    data class Success<out T>(val data: T?) : DataState<T>()
    data class Loading<out T>(val partialData: T? = null) : DataState<T>()
    data class Failure<out T>(val throwable: Throwable? = null, val partialDataFailure: T? = null) :
        DataState<T>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Loading -> partialData
            is Failure -> partialDataFailure
        }
}