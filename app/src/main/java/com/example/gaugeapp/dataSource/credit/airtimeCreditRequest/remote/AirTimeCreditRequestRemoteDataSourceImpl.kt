package com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote

import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

private const val TAG = "AirTimeCreditRequestRem"

/**
 * Air time credit request remote data source impl
 *
 * @property service
 * @constructor Create empty Air time credit request remote data source impl
 */
@ExperimentalCoroutinesApi
class AirTimeCreditRequestRemoteDataSourceImpl @Inject constructor(
    private val service: AirtimeCreditRequestService
) : BaseRemoteDataSource(), AirTimeCreditRequestRemoteDataSource {

    /**
     * Create airtime credit request
     *
     * @param airtimeCreditRequest
     * @return
     */
    override fun createAirtimeCreditRequest(airtimeCreditRequest: AirtimeCreditRequest) =
        service.createAirtimeCreditRequest(airtimeCreditRequest)

    /**
     * Get activated airtime credit request
     *
     * if some request is pending
     *
     * @return
     */
    override fun getLastAirtimeCreditRequest(currentCreditLineId: String) =
        getResult { service.getLastAirtimeCreditRequest(currentCreditLineId) }

    /**
     * Get last airtime credit request real time
     *
     * @param currentCreditLineId
     * @return
     */
    override fun getLastAirtimeCreditRequestRealTime(currentCreditLineId: String) =
        getResult { service.getLastAirtimeCreditRequestRealTime(currentCreditLineId) }

    /**
     * Update airtime credit request
     *
     * @param currentAirtimeCreditRequest
     * @return
     */
    override fun updateAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest) =
        service.updateAirtimeCreditRequest(currentAirtimeCreditRequest)
}