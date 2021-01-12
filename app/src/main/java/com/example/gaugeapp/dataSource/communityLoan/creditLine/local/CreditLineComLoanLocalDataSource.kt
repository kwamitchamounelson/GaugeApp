package com.example.gaugeapp.dataSource.communityLoan.creditLine.local

import com.example.gaugeapp.entities.communityLoan.CreditLineComLoan
import kotlinx.coroutines.flow.Flow

/**
 * Credit line community loan local data source
 *
 * @constructor Create empty Credit line com loan local data source
 */
interface CreditLineComLoanLocalDataSource {
    /**
     * Insert credit line
     *
     * @param creditLineComLoan
     */
    suspend fun insertCreditLine(creditLineComLoan: CreditLineComLoan)

    /**
     * Insert many credit line
     *
     * @param creditLineComLoanList
     */
    suspend fun insertManyCreditLine(creditLineComLoanList: List<CreditLineComLoan>)

    /**
     * Update credit line
     *
     * @param creditLineComLoan
     */
    suspend fun updateCreditLine(creditLineComLoan: CreditLineComLoan)

    /**
     * Update many credit lines
     *
     * @param creditLineComLoanList
     */
    suspend fun updateManyCreditLines(creditLineComLoanList: List<CreditLineComLoan>)

    /**
     * Delete credit line
     *
     * @param creditLineComLoan
     */
    suspend fun deleteCreditLine(creditLineComLoan: CreditLineComLoan)

    /**
     * Delete many credit lines
     *
     * @param creditLineComLoanList
     */
    suspend fun deleteManyCreditLines(creditLineComLoanList: List<CreditLineComLoan>)

    /**
     * Get all credit lines of the user
     *
     * @return
     */
    fun getAllCreditLines(): Flow<List<CreditLineComLoan>>

    /**
     * Get current credit line of users
     *
     * @return
     */
    fun getCurrentCreditLineOfUsers(): Flow<List<CreditLineComLoan>>
}