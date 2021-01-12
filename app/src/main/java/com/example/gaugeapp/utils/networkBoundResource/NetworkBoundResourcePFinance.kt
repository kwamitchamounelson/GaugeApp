package com.example.gaugeapp.utils.networkBoundResource

import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

@InternalCoroutinesApi
inline fun <DB, REMOTE> NetworkBoundResourcePFinance(
    crossinline fetchFromLocal: () -> Flow<DB>, //It fecth data from local database
    crossinline shouldFetchFromRemote: (DB?) -> Boolean = { true }, //It decide whether network request should be made or use local persistent data if available (Optional)
    crossinline fetchFromRemote: () -> Flow<DataState<REMOTE>>, //It perform network request operation
    crossinline processRemoteResponse: (response: DataState<REMOTE>) -> REMOTE, //It process result of network response before saving model class in database
    crossinline saveRemoteData: (REMOTE) -> Unit = { Unit }, //It saves result of network request to local persistent database
    crossinline onFetchFailed: (errorBody: String?, statusCode: Int) -> Unit = { _: String?, _: Int -> Unit } // It handle network request failure scenario (Non HTTP 200..300 response, exceptions etc)
) = flow<DataState<DB>> {

    emit(DataState.Loading(null))

    val localData = fetchFromLocal().first()

    if (shouldFetchFromRemote(localData)) {

        emit(DataState.Loading(localData))

        fetchFromRemote().collect { apiResponse ->
            when (apiResponse) {

                is DataState.Success -> {
                    saveRemoteData(processRemoteResponse(apiResponse))
                    emitAll(fetchFromLocal().map { dbData ->
                        DataState.Success(dbData)
                    })
                }

                is DataState.Failure -> {
                    onFetchFailed(apiResponse.throwable?.message, 0)
                    emitAll(fetchFromLocal().map {
                        DataState.Failure(apiResponse.throwable, it)
                    })
                }
                else -> {

                }
            }
        }
    } else {
        emitAll(fetchFromLocal().map { DataState.Success(it) })
    }
}