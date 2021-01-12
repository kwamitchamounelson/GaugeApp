package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.mappers.ShoppingCreditLineLocalMapper
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.mappers.ShoppingCreditLocalMapper
import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.entities.ShoppingCreditLineWithShoppingCreditsList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Shopping credit local data source implementation
 *
 * @property dao
 * @property creditLineMapper
 * @property creditMapper
 * @constructor Create empty Shopping credit local data source impl
 */
class ShoppingCreditLocalDataSourceImpl @Inject constructor(
    private val dao: ShoppingCreditDao,
    private val creditLineMapper: ShoppingCreditLineLocalMapper,
    private val creditMapper: ShoppingCreditLocalMapper
) : IShoppingCreditLocalDataSource {

    /**
     * Insert shopping credit line
     *
     * @param shoppingCreditLine
     * @return Long
     */
    override suspend fun insertShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) =
        dao.insertShoppingCreditLine(creditLineMapper.mapFromEntity(shoppingCreditLine))

    /**
     * Insert shopping credit
     *
     * @param shoppingCredit
     * @return Long
     */
    override suspend fun insertShoppingCredit(shoppingCredit: ShoppingCredit) =
        dao.insertShoppingCredit(creditMapper.mapFromEntity(shoppingCredit))

    /**
     * Get shopping credit line with shopping credits
     *
     * @return List<ShoppingCreditLineWithShoppingCreditsList>
     */
    override suspend fun getShoppingCreditLineWithShoppingCredits(): List<ShoppingCreditLineWithShoppingCreditsList> {
        val shoppingCreditLineWithCreditPersistList = dao.getShoppingCreditLineWithShoppingCredits()

        return shoppingCreditLineWithCreditPersistList.map {
            ShoppingCreditLineWithShoppingCreditsList(
                creditLineMapper.mapToEntity(it.shoppingCreditLineTable),
                creditMapper.mapListToEntity(it.creditTables)
            )
        }
    }

    /**
     * Get all shopping credit lines
     *
     * @return Flow<List<ShoppingCreditLine>>
     */
    override fun getAllShoppingCreditLines(): Flow<List<ShoppingCreditLine>> =
        dao.getAllShoppingCreditLines().map {
            creditLineMapper.mapListToEntity(it)
        }

    /**
     * Get last shopping credit line
     *
     * @return ShoppingCreditLine
     */
    override suspend fun getLastShoppingCreditLine(): ShoppingCreditLine =
        creditLineMapper.mapToEntity(dao.getLastShoppingCreditLine())

    /**
     * Get all unsolved shopping credits
     *
     * @return Flow<ShoppingCredit>
     */
    override fun getAllUnsolvedShoppingCredits(): Flow<ShoppingCredit> =
        dao.getAllUnsolvedShoppingCredits().map { creditMapper.mapToEntity(it) }

    /**
     * Get all solved shopping credits
     *
     * @return Flow<ShoppingCredit>
     */
    override fun getAllSolvedShoppingCredits(): Flow<ShoppingCredit> =
        dao.getAllSolvedShoppingCredits().map { creditMapper.mapToEntity(it) }


}