package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote

import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

/**
 * Air time credit line remote data source impl
 *
 * @property service
 * @constructor Create empty Air time credit line remote data source impl
 */

@ExperimentalCoroutinesApi
class AirTimeCreditLineRemoteDataSourceImpl @Inject constructor(
    private val service: AirtimeCreditLineService
) : BaseRemoteDataSource(), AirTimeCreditLineRemoteDataSource {


    /**
     * Create airtime credit line
     *
     * @param airtimeCreditLine
     */
    override fun createAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) =
        service.createAirtimeCreditLine(airtimeCreditLine)


    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    override fun updateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) =
        service.updateAirtimeCreditLine(airtimeCreditLine)

    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    override fun updateAirtimeCreditLine(
        airtimeCreditLine: AirTimeCreditLine,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) = service.updateAirtimeCreditLine(airtimeCreditLine, onSuccess, onError)


    /**
     * Get all airtime credit line
     *
     * @return
     */
    override fun getAllAirtimeCreditLine() =
        getResult { service.getAllAirtimeCreditLine() }

    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    override fun getAllSolvedCreditLineOfTheUser() =
        getResult { service.getAllSolvedCreditLineOfTheUser() }

    /**
     * Get all solved credit line of the user start after limit
     *
     * @param creationDate
     * @param limit
     */
    override fun getAllSolvedCreditLineOfTheUserStartAfterLimit(
        creationDate: Date,
        limit: Long
    ) =
        getResult { service.getAllSolvedCreditLineOfTheUserStartAfterLimit(creationDate, limit) }


    /**
     * Get all solved credit line of the user start after
     *
     * @param creationDate
     */
    override fun getAllSolvedCreditLineOfTheUserStartAfter(creationDate: Date) =
        getResult { service.getAllSolvedCreditLineOfTheUserStartAfter(creationDate) }


    /**
     * Get current airtime credit line of the user
     *
     * @return
     */
    override fun getCurrentAirtimeCreditLine() =
        getResult { service.getCurrentAirtimeCreditLine() }

    /**
     * Get current airtime credit line real time
     *
     * @return
     */
    override fun getCurrentAirtimeCreditLineRealTime() =
        getResult { service.getCurrentAirtimeCreditLineRealTime() }

    /**
     * Get current airtime credit line not flow real time
     *
     * @param onComplete
     * @receiver
     */
    override fun getCurrentAirtimeCreditLineNotFlowRealTime(onComplete: (DataState<AirTimeCreditLine?>) -> Unit) =
        service.getCurrentAirtimeCreditLineNotFlowRealTime(onComplete)
}