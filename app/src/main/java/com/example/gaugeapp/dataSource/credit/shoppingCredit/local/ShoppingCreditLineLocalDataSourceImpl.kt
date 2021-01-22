package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import com.example.gaugeapp.entities.ShoppingCreditLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShoppingCreditLineLocalDataSourceImpl @Inject constructor(
    private val mapper: ShoppingCreditLineLocalMapper,
    private val dao: ShoppingCreditLineDao
) : ShoppingCreditLineLocalDataSource {


    /**
     * Insert shopping credit line
     *
     * @param shoppingCreditLine
     */
    override suspend fun insertShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) =
        dao.insertShoppingCreditLine(mapper.mapFromEntity(shoppingCreditLine))


    /**
     * Insert many shopping credit line
     *
     * @param shoppingCreditLineList
     */
    override suspend fun insertManyShoppingCreditLine(shoppingCreditLineList: List<ShoppingCreditLine>) =
        dao.insertAllShoppingCreditLine(mapper.mapListFromEntity(shoppingCreditLineList))


    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     */
    override suspend fun updateShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) =
        dao.updateShoppingCreditLine(mapper.mapFromEntity(shoppingCreditLine))

    /**
     * Update many shopping credit line
     *
     * @param shoppingCreditLine
     */
    override suspend fun updateManyShoppingCreditLine(shoppingCreditLine: List<ShoppingCreditLine>) =
        dao.updateAllShoppingCreditLine(mapper.mapListFromEntity(shoppingCreditLine))


    /**
     * Delete shopping credit line
     *
     * @param hoppingCreditLine
     */
    override suspend fun deleteShoppingCreditLine(hoppingCreditLine: ShoppingCreditLine) =
        dao.deleteShoppingCreditLine(mapper.mapFromEntity(hoppingCreditLine))


    /**
     * Delete many shopping credit line
     *
     * @param shoppingCreditLine
     */
    override suspend fun deleteManyShoppingCreditLine(shoppingCreditLine: List<ShoppingCreditLine>) =
        dao.deleteAllShoppingCreditLine(mapper.mapListFromEntity(shoppingCreditLine))

    /**
     * Get all shopping credit line
     *
     * @return
     */
    override suspend fun getAllShoppingCreditLine(): List<ShoppingCreditLine> =
        mapper.mapListToEntity(dao.getAllShoppingCreditLine())

    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    override fun getAllSolvedCreditLineOfTheUser(): Flow<List<ShoppingCreditLine>> =
        dao.getAllSolvedCreditLineOfTheUser().map {
            mapper.mapListToEntity(it)
        }

}