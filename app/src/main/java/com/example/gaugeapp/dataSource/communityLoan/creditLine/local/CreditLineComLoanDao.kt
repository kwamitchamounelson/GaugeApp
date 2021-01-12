package com.example.gaugeapp.dataSource.communityLoan.creditLine.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface CreditLineComLoanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditLine(creditLineComLoanTable: CreditLineComLoanTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManyCreditLine(creditLineComLoanTableList: List<CreditLineComLoanTable>)

    @Update
    suspend fun updateCreditLine(creditLineComLoanTable: CreditLineComLoanTable)

    @Update
    suspend fun updateManyCreditLines(creditLineComLoanTableList: List<CreditLineComLoanTable>)

    @Delete
    suspend fun deleteCreditLine(creditLineComLoanTable: CreditLineComLoanTable)

    @Delete
    suspend fun deleteManyCreditLines(creditLineComLoanTableList: List<CreditLineComLoanTable>)


    /**
     * Get all credit lines of the user
     *
     * @return
     */
    @Query("SELECT * FROM creditlinecomloantable ORDER BY createDate DESC")
    fun getAllCreditLines(): Flow<List<CreditLineComLoanTable>>

    /**
     * Get current credit line of users
     *
     * @return
     */
    @Query("SELECT * FROM creditlinecomloantable WHERE solved=1 ORDER BY createDate DESC LIMIT 1")
    fun getCurrentCreditLineOfUsers(): Flow<List<CreditLineComLoanTable>>
}