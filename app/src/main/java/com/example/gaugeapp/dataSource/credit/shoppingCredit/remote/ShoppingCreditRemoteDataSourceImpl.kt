package com.example.gaugeapp.dataSource.credit.shoppingCredit.remote

import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import java.util.*
import javax.inject.Inject

class ShoppingCreditRemoteDataSourceImpl @Inject constructor(
    private val service: ShoppingCreditService
) :
    BaseRemoteDataSource(), IShoppingCreditRemoteDataSource {

    /**
     * Create shopping credit line
     *
     * @param shoppingCreditLine
     */
    override fun createShoppingCreditLine(
        shoppingCreditLine: ShoppingCreditLine
    ) = getResult { service.createShoppingCreditLine(shoppingCreditLine) }

    /**
     * Update shopping credit line
     *
     * @param updatedShoppingCreditLine
     */
    override fun updateShoppingCreditLine(
        updatedShoppingCreditLine: ShoppingCreditLine
    ) = getResult { service.updateShoppingCreditLine(updatedShoppingCreditLine) }

    /**
     * Get all shopping credit lines of user
     *
     */
    override fun getAllShoppingCreditLinesOfUser() =
        getResult { service.getAllShoppingCreditLinesOfUser() }

    /**
     * Get shop credit lines of user greater than update date
     *
     * @param latestRead
     */
    override fun getShopCreditLinesOfUserGreaterThanUpdateDate(latestRead: Date) =
        getResult { service.getShopCreditLinesOfUserGreaterThanUpdateDate(latestRead) }

    /**
     * Create shopping credit
     *
     * @param shoppingCreditLineUid
     * @param shoppingCredit
     */
    override fun createShoppingCredit(
        shoppingCreditLineUid: String,
        shoppingCredit: ShoppingCredit
    ) = getResult { service.createShoppingCredit(shoppingCreditLineUid, shoppingCredit) }

    /**
     * Update shopping credit
     *
     * @param shoppingCreditLineUid
     * @param updatedShoppingCredit
     */
    override fun updateShoppingCredit(
        shoppingCreditLineUid: String,
        updatedShoppingCredit: ShoppingCredit
    ) = getResult { service.updateShoppingCredit(shoppingCreditLineUid, updatedShoppingCredit) }

    /**
     * Get all shopping credit in credit line of user
     *
     * @param shoppingCreditLineUid
     */
    override fun getAllShoppingCreditInCreditLineOfUser(shoppingCreditLineUid: String) =
        getResult { service.getAllShoppingCreditInCreditLineOfUser(shoppingCreditLineUid) }

    /**
     * Get all shopping credit of user
     *
     */
    override fun getAllShoppingCreditOfUser() = getResult { service.getAllShoppingCreditOfUser() }

    /**
     * Get shop credit of user greater than update date
     * Permet de recupérer
     * toutes les nouvelles lignes de  credit du serveur,
     * et les lignes de  credit mises à jours
     * @param date : la date à partir de laquelle on aimerait
     * recupérer les lignes de  credit
     * @return FirebaseResponseType<List<FinanceEducationArticle>>
     */
    override fun getShopCreditOfUserGreaterThanUpdateDate(date: Date) =
        getResult { service.getShopCreditOfUserGreaterThanUpdateDate(date) }


    /**
     * Get
     * all stores
     *
     */
    override fun getAllStores() = getResult { service.getAllStores() }

    /**
     * Get single store
     *
     * @param storeUid
     */
    override fun getSingleStore(storeUid: String) = getResult { service.getSingleStore(storeUid) }


}