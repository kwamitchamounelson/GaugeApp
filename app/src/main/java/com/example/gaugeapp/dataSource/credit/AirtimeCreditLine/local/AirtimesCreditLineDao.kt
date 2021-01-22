package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AirtimesCreditLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirtimeCreditLine(airtimesCreditLineLocalEntity: AirtimesCreditLineLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAirtimeCreditLine(airtimesCreditLineLocalEntityList: List<AirtimesCreditLineLocalEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAirtimeCreditLine(airtimesCreditLineLocalEntity: AirtimesCreditLineLocalEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAllAirtimeCreditLine(airtimesCreditLineLocalEntityList: List<AirtimesCreditLineLocalEntity>)

    @Delete
    suspend fun deleteAirtimeCreditLine(airtimesCreditLineLocalEntity: AirtimesCreditLineLocalEntity)

    @Delete
    suspend fun deleteAllAirtimeCreditLine(airtimesCreditLineLocalEntityList: List<AirtimesCreditLineLocalEntity>)

    @Query("SELECT * FROM airtimescreditlinelocalentity ORDER BY createAt DESC")
    suspend fun getAllAirtimeCreditLine(): List<AirtimesCreditLineLocalEntity>

    @Query("SELECT * FROM airtimescreditlinelocalentity WHERE solved=1 ORDER BY createAt DESC")
    fun getAllSolvedCreditLineOfTheUser(): Flow<List<AirtimesCreditLineLocalEntity>>
}