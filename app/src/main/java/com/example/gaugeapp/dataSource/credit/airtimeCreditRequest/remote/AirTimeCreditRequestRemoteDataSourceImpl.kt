package com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote

import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.utils.DataState
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
     * Get last airtime credit request not flow real time
     *
     * @param currentCreditLineId
     * @param onComplete
     * @receiver
     */
    override fun getLastAirtimeCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<AirtimeCreditRequest?>) -> Unit
    ) = service.getLastAirtimeCreditRequestNotFlowRealTime(currentCreditLineId, onComplete)

    /**
     * Update airtime credit request
     *
     * @param currentAirtimeCreditRequest
     * @return
     */
    override fun updateAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest) =
        service.updateAirtimeCreditRequest(currentAirtimeCreditRequest)

    /**
     * Update airtime credit request
     *
     * @param currentAirtimeCreditRequest
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    override fun updateAirtimeCreditRequest(
        currentAirtimeCreditRequest: AirtimeCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) =
        service.updateAirtimeCreditRequest(currentAirtimeCreditRequest, onSuccess, onError)
}