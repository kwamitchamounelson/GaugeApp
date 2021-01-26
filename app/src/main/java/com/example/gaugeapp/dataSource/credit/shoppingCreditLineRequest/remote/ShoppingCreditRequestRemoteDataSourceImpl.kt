package com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote

import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

private const val TAG = "ShoppingCreditRequestRem"

/**
 * Air time credit request remote data source impl
 *
 * @property service
 * @constructor Create empty Air time credit request remote data source impl
 */
@ExperimentalCoroutinesApi
class ShoppingCreditRequestRemoteDataSourceImpl @Inject constructor(
    private val service: ShoppingCreditRequestService
) : BaseRemoteDataSource(), ShoppingCreditRequestRemoteDataSource {

    /**
     * Create shopping credit request
     *
     * @param shoppingCreditRequest
     */
    override fun createShoppingCreditRequest(
        shoppingCreditRequest: ShoppingCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) =
        service.createShoppingCreditRequest(shoppingCreditRequest, onSuccess, onError)

    /**
     * Get last shopping credit request
     *
     * @param currentCreditLineId
     */
    override fun getLastShoppingCreditRequest(currentCreditLineId: String) =
        getResult { service.getLastShoppingCreditRequest(currentCreditLineId) }

    /**
     * Get last shopping credit request real time
     *
     * @param currentCreditLineId
     */
    override fun getLastShoppingCreditRequestRealTime(currentCreditLineId: String) =
        getResult { service.getLastShoppingCreditRequestRealTime(currentCreditLineId) }

    /**
     * Get last shopping credit request not flow real time
     *
     * @param currentCreditLineId
     * @param onComplete
     * @receiver
     */
    override fun getLastShoppingCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<ShoppingCreditRequest?>) -> Unit
    ) = service.getLastShoppingCreditRequestNotFlowRealTime(currentCreditLineId, onComplete)

    /**
     * Update shopping credit request
     *
     * @param currentShoppingCreditRequest
     */
    override fun updateShoppingCreditRequest(currentShoppingCreditRequest: ShoppingCreditRequest) =
        service.updateShoppingCreditRequest(currentShoppingCreditRequest)

    /**
     * Update shopping credit request
     *
     * @param currentShoppingCreditRequest
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    override fun updateShoppingCreditRequest(
        currentShoppingCreditRequest: ShoppingCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) =
        service.updateShoppingCreditRequest(currentShoppingCreditRequest, onSuccess, onError)
}