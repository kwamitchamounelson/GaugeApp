package com.example.gaugeapp.dataSource.communityLoan.creditLine.remote

import com.example.gaugeapp.entities.communityLoan.CreditLineComLoan
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow

/**
 * Credit line com loan remote data source
 *
 * @constructor Create empty Credit line com loan remote data source
 */
interface CreditLineComLoanRemoteDataSource {
    /**
     * Create credit line com loan
     *
     * @param creditLineComLoan
     * @return
     */
    fun createCreditLineComLoan(creditLineComLoan: CreditLineComLoan): Flow<DataState<CreditLineComLoan>>

    /**
     * Update credit line com loan
     *
     * @param creditLineComLoan
     * @return
     */
    fun UpdateCreditLineComLoan(creditLineComLoan: CreditLineComLoan): Flow<DataState<CreditLineComLoan>>

    /**
     * Get all credit line com loan
     *
     * @return
     */
    fun getAllCreditLineComLoan(): Flow<DataState<List<CreditLineComLoan>>>

    /**
     * Get 1current credit line com loan
     * returns the active credit line the user is on
     * @return
     */
    fun getCurrentCreditLineComLoan(): Flow<DataState<List<CreditLineComLoan>>>
}