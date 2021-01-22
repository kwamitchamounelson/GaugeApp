package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import com.example.gaugeapp.entities.ShoppingCreditLine
import kotlinx.coroutines.flow.Flow

interface ShoppingCreditLineLocalDataSource {

    /**
     * Insert shopping credit line
     *
     * @param shoppingCreditLine
     */
    suspend fun insertShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine)

    /**
     * Insert many shopping credit line
     *
     * @param shoppingCreditLineList
     */
    suspend fun insertManyShoppingCreditLine(shoppingCreditLineList: List<ShoppingCreditLine>)

    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     */
    suspend fun updateShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine)

    /**
     * Update many shopping credit line
     *
     * @param shoppingCreditLine
     */
    suspend fun updateManyShoppingCreditLine(shoppingCreditLine: List<ShoppingCreditLine>)

    /**
     * Delete shopping credit line
     *
     * @param hoppingCreditLine
     */
    suspend fun deleteShoppingCreditLine(hoppingCreditLine: ShoppingCreditLine)

    /**
     * Delete many shopping credit line
     *
     * @param shoppingCreditLine
     */
    suspend fun deleteManyShoppingCreditLine(shoppingCreditLine: List<ShoppingCreditLine>)

    /**
     * Get all shopping credit line
     *
     * @return
     */
    suspend fun getAllShoppingCreditLine(): List<ShoppingCreditLine>

    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    fun getAllSolvedCreditLineOfTheUser(): Flow<List<ShoppingCreditLine>>
}