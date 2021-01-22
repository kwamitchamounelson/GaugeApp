package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCreditLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingCreditLine(shoppingCreditLineLocalEntity: ShoppingCreditLineLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllShoppingCreditLine(shoppingCreditLineLocalEntityList: List<ShoppingCreditLineLocalEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateShoppingCreditLine(shoppingCreditLineLocalEntity: ShoppingCreditLineLocalEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAllShoppingCreditLine(shoppingCreditLineLocalEntityList: List<ShoppingCreditLineLocalEntity>)

    @Delete
    suspend fun deleteShoppingCreditLine(shoppingCreditLineLocalEntity: ShoppingCreditLineLocalEntity)

    @Delete
    suspend fun deleteAllShoppingCreditLine(shoppingCreditLineLocalEntityList: List<ShoppingCreditLineLocalEntity>)

    @Query("SELECT * FROM shoppingcreditlinelocalentity ORDER BY createAt DESC")
    suspend fun getAllShoppingCreditLine(): List<ShoppingCreditLineLocalEntity>

    @Query("SELECT * FROM shoppingcreditlinelocalentity WHERE solved=1 ORDER BY createAt DESC")
    fun getAllSolvedCreditLineOfTheUser(): Flow<List<ShoppingCreditLineLocalEntity>>
}