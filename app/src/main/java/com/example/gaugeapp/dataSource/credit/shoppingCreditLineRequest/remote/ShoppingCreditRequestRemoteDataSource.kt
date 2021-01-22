package com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote

import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Shopping credit request remote data source
 *
 * @constructor Create empty Shopping credit request remote data source
 */
interface ShoppingCreditRequestRemoteDataSource {
    /**
     * Create shopping credit request
     *
     * @param shoppingCreditRequest
     */
    fun createShoppingCreditRequest(shoppingCreditRequest: ShoppingCreditRequest)


    /**
     * Get last shopping credit request
     *
     * @param currentCreditLineId
     * @return
     */
    fun getLastShoppingCreditRequest(currentCreditLineId: String): Flow<DataState<ShoppingCreditRequest?>>

    /**
     * Get last shopping credit request real time
     *
     * @param currentCreditLineId
     * @return
     */
    fun getLastShoppingCreditRequestRealTime(currentCreditLineId: String): Flow<DataState<ShoppingCreditRequest?>>

    /**
     * Get last shopping credit request not flow real time
     *
     * @param currentCreditLineId
     * @param onComplete
     * @receiver
     */
    fun getLastShoppingCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<ShoppingCreditRequest?>) -> Unit
    )

    /**
     * Update shopping credit request
     *
     * @param currentShoppingCreditRequest
     */
    fun updateShoppingCreditRequest(currentShoppingCreditRequest: ShoppingCreditRequest)


    /**
     * Update shopping credit request
     *
     * @param currentShoppingCreditRequest
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    fun updateShoppingCreditRequest(
        currentShoppingCreditRequest: ShoppingCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )
}