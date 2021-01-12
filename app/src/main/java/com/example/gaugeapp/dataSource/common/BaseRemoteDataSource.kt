package com.example.gaugeapp.dataSource.common

import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.FirebaseResponseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * @author Nelson Kwami
 * cette classe permet de router toutes nos donnees distantes en DataState
 */
abstract class BaseRemoteDataSource {

    protected fun <T> getResult(call: () -> Flow<FirebaseResponseType<T>>): Flow<DataState<T>> =
        flow {
            call().collect {
                when (it) {
                    is FirebaseResponseType.FirebaseSuccessResponse -> {
                        emit(DataState.Success(it.body))
                    }
                    is FirebaseResponseType.FirebaseErrorResponse -> {
                        emit(DataState.Failure(it.throwable))
                    }
                    is FirebaseResponseType.FirebaseEmptyResponse->{
                        emit(DataState.Success(it.body))
                    }
                }
            }
        }
}