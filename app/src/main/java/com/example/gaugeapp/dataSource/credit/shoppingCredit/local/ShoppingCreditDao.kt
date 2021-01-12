package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import androidx.room.*
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditLineTable
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditLineWithShoppingCreditsListPersist
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditTable
import kotlinx.coroutines.flow.Flow



@Dao
interface ShoppingCreditDao {

    /**
     * Insert shopping credit line
     *
     * @param shoppingCreditLineTable
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingCreditLine(shoppingCreditLineTable: ShoppingCreditLineTable):Long

    /**
     * Insert shopping credit
     *
     * @param shoppingCreditTable
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingCredit(shoppingCreditTable: ShoppingCreditTable):Long

    /**
     * Get shopping credit line with shopping credits
     *
     * @return
     */
    @Transaction
    @Query("SELECT * FROM ShoppingCreditLineTable")
    suspend fun getShoppingCreditLineWithShoppingCredits(): List<ShoppingCreditLineWithShoppingCreditsListPersist>

    /**
     * Get all shopping credit lines
     *
     */
    @Query("SELECT * FROM ShoppingCreditLineTable")
    fun getAllShoppingCreditLines():Flow<List<ShoppingCreditLineTable>>

    /**
     * Get last shopping credit line
     *
     * @return
     */
    @Query("SELECT * FROM ShoppingCreditLineTable ORDER BY  createAt DESC limit 1")
    suspend fun getLastShoppingCreditLine(): ShoppingCreditLineTable

    /**
     * Get all unsolved shopping credits
     *
     * @return
     */
    @Query("SELECT * FROM ShoppingCreditTable WHERE _isSolved = 0") //When you store boolean using room, it automatically stores 1 for true and 0 for false.
    fun getAllUnsolvedShoppingCredits(): Flow<ShoppingCreditTable>

    /**
     * Get all solved shopping credits
     *
     * @return
     */
    @Query("SELECT * FROM ShoppingCreditTable WHERE _isSolved = 1") //When you store boolean using room, it automatically stores 1 for true and 0 for false.
    fun getAllSolvedShoppingCredits(): Flow<ShoppingCreditTable>
}