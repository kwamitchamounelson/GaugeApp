package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StoreRepository @Inject constructor() {
    fun getAllStores(): Flow<DataState<List<Store>>> {
        //mock data
        return flow {
            val list = (0..100).map {
                Store()
            }

            emit(DataState.Success(list))
        }
    }
}