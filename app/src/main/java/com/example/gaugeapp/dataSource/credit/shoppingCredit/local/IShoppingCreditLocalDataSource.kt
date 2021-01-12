package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.entities.ShoppingCreditLineWithShoppingCreditsList
import kotlinx.coroutines.flow.Flow

interface IShoppingCreditLocalDataSource {
    /**
     * Insert shopping credit line
     *
     * @param shoppingCreditLine
     * @return Long
     */
    suspend fun insertShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine): Long

    /**
     * Insert shopping credit
     *
     * @param shoppingCredit
     * @return Long
     */
    suspend fun insertShoppingCredit(shoppingCredit: ShoppingCredit): Long

    /**
     * Get shopping credit line with shopping credits
     *
     * @return List<ShoppingCreditLineWithShoppingCreditsList>
     */
    suspend fun getShoppingCreditLineWithShoppingCredits(): List<ShoppingCreditLineWithShoppingCreditsList>

    /**
     * Get all shopping credit lines
     *
     * @return Flow<List<ShoppingCreditLine>>
     */
    fun getAllShoppingCreditLines(): Flow<List<ShoppingCreditLine>>

    /**
     * Get last shopping credit line
     *
     * @return ShoppingCreditLine
     */
    suspend fun getLastShoppingCreditLine(): ShoppingCreditLine

    /**
     * Get all unsolved shopping credits
     *
     * @return Flow<ShoppingCredit>
     */
    fun getAllUnsolvedShoppingCredits(): Flow<ShoppingCredit>

    /**
     * Get all solved shopping credits
     *
     * @return Flow<ShoppingCredit>
     */
    fun getAllSolvedShoppingCredits(): Flow<ShoppingCredit>
}