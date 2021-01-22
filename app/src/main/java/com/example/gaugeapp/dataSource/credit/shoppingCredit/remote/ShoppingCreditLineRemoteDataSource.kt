package com.example.gaugeapp.dataSource.credit.shoppingCredit.remote

import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import java.util.*


/**
 * Shopping credit line remote data source
 *
 * @constructor Create empty Shopping credit line remote data source
 */
interface ShoppingCreditLineRemoteDataSource {
    /**
     * Create shopping credit line
     *
     * @param shoppingCreditLine
     */
    fun createShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine)

    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     */
    fun updateShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine)

    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    fun updateShoppingCreditLine(
        shoppingCreditLine: ShoppingCreditLine,
        onSuccess: () -> Unit,
        onError: () -> Unit
    )

    /**
     * Get all shopping credit line
     *
     * @return
     */
    fun getAllShoppingCreditLine(): Flow<DataState<List<ShoppingCreditLine>>>

    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    fun getAllSolvedCreditLineOfTheUser(): Flow<DataState<List<ShoppingCreditLine>>>

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
    ): Flow<DataState<List<ShoppingCreditLine>>>


    /**
     * Get all solved credit line of the user start after
     *
     * @param creationDate
     * @return
     */
    fun getAllSolvedCreditLineOfTheUserStartAfter(creationDate: Date): Flow<DataState<List<ShoppingCreditLine>>>

    /**
     * Get current shopping credit line
     *
     * @return
     */
    fun getCurrentShoppingCreditLine(): Flow<DataState<ShoppingCreditLine?>>


    /**
     * Get current shopping credit line real time
     *
     * @return
     */
    fun getCurrentShoppingCreditLineRealTime(): Flow<DataState<ShoppingCreditLine?>>


    /**
     * Get current shopping credit line not flow real time
     *
     * @param onComplete
     * @receiver
     */
    fun getCurrentShoppingCreditLineNotFlowRealTime(onComplete: (DataState<ShoppingCreditLine?>) -> Unit)
}