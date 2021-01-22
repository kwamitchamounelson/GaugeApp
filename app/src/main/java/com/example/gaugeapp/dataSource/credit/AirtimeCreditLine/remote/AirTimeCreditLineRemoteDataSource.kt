package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote

import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import java.util.*


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
    fun createAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine)

    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    fun updateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine)

    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    fun updateAirtimeCreditLine(
        airtimeCreditLine: AirTimeCreditLine,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )

    /**
     * Get all airtime credit line
     *
     * @return
     */
    fun getAllAirtimeCreditLine(): Flow<DataState<List<AirTimeCreditLine>>>

    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    fun getAllSolvedCreditLineOfTheUser(): Flow<DataState<List<AirTimeCreditLine>>>

    /**
     * Get all solved credit line of the user start after limit
     *
     * @param creationDate
     * @param limit
     * @return
     */
    fun getAllSolvedCreditLineOfTheUserStartAfterLimit(
        creationDate: Date,
        limit: Long
    ): Flow<DataState<List<AirTimeCreditLine>>>


    /**
     * Get all solved credit line of the user start after
     *
     * @param creationDate
     * @return
     */
    fun getAllSolvedCreditLineOfTheUserStartAfter(creationDate: Date): Flow<DataState<List<AirTimeCreditLine>>>

    /**
     * Get current airtime credit line of the user
     *
     * @return
     */
    fun getCurrentAirtimeCreditLine(): Flow<DataState<AirTimeCreditLine?>>


    /**
     * Get current airtime credit line real time
     *
     * @return
     */
    fun getCurrentAirtimeCreditLineRealTime(): Flow<DataState<AirTimeCreditLine?>>


    /**
     * Get current airtime credit line not flow real time
     *
     * @param onComplete
     * @receiver
     */
    fun getCurrentAirtimeCreditLineNotFlowRealTime(onComplete: (DataState<AirTimeCreditLine?>) -> Unit)
}