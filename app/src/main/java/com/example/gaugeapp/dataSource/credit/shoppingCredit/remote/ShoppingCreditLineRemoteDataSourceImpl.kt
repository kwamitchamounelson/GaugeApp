package com.example.gaugeapp.dataSource.credit.shoppingCredit.remote

import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

/**
 * Shopping credit line remote data source impl
 *
 * @property service
 * @constructor Create empty Shopping credit line remote data source impl
 */

@ExperimentalCoroutinesApi
class ShoppingCreditLineRemoteDataSourceImpl @Inject constructor(
    private val service: ShoppingCreditLineService
) : BaseRemoteDataSource(), ShoppingCreditLineRemoteDataSource {


    /**
     * Create shopping credit line
     *
     * @param shoppingCreditLine
     */
    override fun createShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) =
        service.createShoppingCreditLine(shoppingCreditLine)


    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     */
    override fun updateShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) =
        service.updateShoppingCreditLine(shoppingCreditLine)

    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    override fun updateShoppingCreditLine(
        shoppingCreditLine: ShoppingCreditLine,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) = service.updateShoppingCreditLine(shoppingCreditLine, onSuccess, onError)


    /**
     * Get all shopping credit line
     *
     */
    override fun getAllShoppingCreditLine() =
        getResult { service.getAllShoppingCreditLine() }

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
     * Get current shopping credit line
     *
     */
    override fun getCurrentShoppingCreditLine() =
        getResult { service.getCurrentShoppingCreditLine() }

    /**
     * Get current shopping credit line real time
     *
     */
    override fun getCurrentShoppingCreditLineRealTime() =
        getResult { service.getCurrentShoppingCreditLineRealTime() }

    /**
     * Get current shopping credit line not flow real time
     *
     * @param onComplete
     * @receiver
     */
    override fun getCurrentShoppingCreditLineNotFlowRealTime(onComplete: (DataState<ShoppingCreditLine?>) -> Unit) =
        service.getCurrentShoppingCreditLineNotFlowRealTime(onComplete)
}