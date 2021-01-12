package com.example.gaugeapp.dataSource.credit.shoppingCredit.remote

import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import java.util.*

interface IShoppingCreditRemoteDataSource {
    /**
     * Create shopping credit line
     *
     * @param shoppingCreditLine
     */
    fun createShoppingCreditLine(
        shoppingCreditLine: ShoppingCreditLine
    ): Flow<DataState<ShoppingCreditLine>>

    /**
     * Update shopping credit line
     *
     * @param updatedShoppingCreditLine
     */
    fun updateShoppingCreditLine(
        updatedShoppingCreditLine: ShoppingCreditLine
    ): Flow<DataState<ShoppingCreditLine>>

    /**
     * Get all shopping credit lines of user
     *
     */
    fun getAllShoppingCreditLinesOfUser(): Flow<DataState<List<ShoppingCreditLine>>>

    /**
     * Get shop credit lines of user greater than update date
     *
     * @param latestRead
     */
    fun getShopCreditLinesOfUserGreaterThanUpdateDate(latestRead: Date): Flow<DataState<List<ShoppingCreditLine>>>

    /**
     * Create shopping credit
     *
     * @param shoppingCreditLineUid
     * @param shoppingCredit
     */
    fun createShoppingCredit(
        shoppingCreditLineUid: String,
        shoppingCredit: ShoppingCredit
    ): Flow<DataState<ShoppingCredit>>

    /**
     * Update shopping credit
     *
     * @param shoppingCreditLineUid
     * @param updatedShoppingCredit
     */
    fun updateShoppingCredit(
        shoppingCreditLineUid: String,
        updatedShoppingCredit: ShoppingCredit
    ): Flow<DataState<ShoppingCredit>>

    /**
     * Get all shopping credit in credit line of user
     *
     * @param shoppingCreditLineUid
     */
    fun getAllShoppingCreditInCreditLineOfUser(shoppingCreditLineUid: String): Flow<DataState<List<ShoppingCredit>>>

    /**
     * Get all shopping credit of user
     *
     */
    fun getAllShoppingCreditOfUser(): Flow<DataState<List<ShoppingCredit>>>

    /**
     * Get shop credit of user greater than update date
     * Permet de recupérer
     * toutes les nouvelles lignes de  credit du serveur,
     * et les lignes de  credit mises à jours
     * @param date : la date à partir de laquelle on aimerait
     * recupérer les lignes de  credit
     * @return FirebaseResponseType<List<FinanceEducationArticle>>
     */
    fun getShopCreditOfUserGreaterThanUpdateDate(date: Date): Flow<DataState<List<ShoppingCredit>>>

    /**
     * Get
     * all stores
     *
     */
    fun getAllStores(): Flow<DataState<List<Store>>>

    /**
     * Get single store
     *
     * @param storeUid
     */
    fun getSingleStore(storeUid: String): Flow<DataState<Store>>
}