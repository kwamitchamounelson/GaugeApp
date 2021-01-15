package com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote

import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Air time credit request remote data source
 *
 * @constructor Create empty Air time credit request remote data source
 */
interface AirTimeCreditRequestRemoteDataSource {
    /**
     * Create airtime credit request
     *
     * @param airtimeCreditRequest
     * @return
     */
    fun createAirtimeCreditRequest(airtimeCreditRequest: AirtimeCreditRequest): Flow<DataState<AirtimeCreditRequest>>


    /**
     * Get activated airtime credit request
     *
     * if some request is pending
     *
     * @return
     */
    fun getLastAirtimeCreditRequest(currentCreditLineId: String): Flow<DataState<AirtimeCreditRequest?>>

    /**
     * Update airtime credit request
     *
     * @param currentAirtimeCreditRequest
     * @return
     */
    fun updateAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest): Flow<DataState<AirtimeCreditRequest?>>
}