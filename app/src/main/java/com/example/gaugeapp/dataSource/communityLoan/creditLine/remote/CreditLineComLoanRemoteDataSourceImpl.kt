package com.example.gaugeapp.dataSource.communityLoan.creditLine.remote

import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.entities.communityLoan.CreditLineComLoan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Credit line com loan remote data source impl
 *
 * @property service
 * @constructor Create empty Credit line com loan remote data source impl
 */
@ExperimentalCoroutinesApi
class CreditLineComLoanRemoteDataSourceImpl @Inject constructor(
    private val service: CreditLineComLoanRemoteService
) : BaseRemoteDataSource(), CreditLineComLoanRemoteDataSource {

    /**
     * Create credit line com loan
     *
     * @param creditLineComLoan
     * @return
     */
    override fun createCreditLineComLoan(creditLineComLoan: CreditLineComLoan) =
        getResult { service.createCreditLineComLoan(creditLineComLoan) }


    /**
     * Update credit line com loan
     *
     * @param creditLineComLoan
     * @return
     */
    override fun UpdateCreditLineComLoan(creditLineComLoan: CreditLineComLoan) =
        getResult { service.UpdateCreditLineComLoan(creditLineComLoan) }


    /**
     * Get all credit line com loan
     *
     * @return
     */
    override fun getAllCreditLineComLoan() =
        getResult { service.getAllCreditLineComLoan() }

    /**
     * Get 1current credit line com loan
     * returns the active credit line the user is on
     * @return
     */
    override fun getCurrentCreditLineComLoan() =
        getResult { service.getCurrentCreditLineComLoan() }
}