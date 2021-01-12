package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote

import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow


/**
 * Air time credit line remote data source
 *
 * @constructor Create empty Air time credit line remote data source
 */
interface AirTimeCreditLineRemoteDataSource {
    /**
     * Create airtime credit line
     *
     * @param airtimeCreditLine
     */
    fun createAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine): Flow<DataState<AirTimeCreditLine>>

    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    fun UpdateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine): Flow<DataState<AirTimeCreditLine>>

    /**
     * Get all airtime credit line
     *
     * @return
     */
    fun getAllAirtimeCreditLine(): Flow<DataState<List<AirTimeCreditLine>>>

    /**
     * Get current airtime credit line of the user
     *
     * @return
     */
    fun getCurrentAirtimeCreditLine(): Flow<DataState<List<AirTimeCreditLine>>>
}