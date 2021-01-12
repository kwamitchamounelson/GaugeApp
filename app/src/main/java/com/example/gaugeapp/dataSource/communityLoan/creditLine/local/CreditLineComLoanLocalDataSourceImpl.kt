package com.example.gaugeapp.dataSource.communityLoan.creditLine.local

import com.example.gaugeapp.entities.communityLoan.CreditLineComLoan
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Credit line community loan local data source implementation
 *
 * @property mapper
 * @property dao
 * @constructor Create empty Credit line com loan local data source impl
 */
class CreditLineComLoanLocalDataSourceImpl @Inject constructor(
    private val mapper: CreditLineComLoanLocalMapper,
    private val dao: CreditLineComLoanDao
) : CreditLineComLoanLocalDataSource {

    /**
     * Insert credit line
     *
     * @param creditLineComLoan
     */
    override suspend fun insertCreditLine(creditLineComLoan: CreditLineComLoan) =
        dao.insertCreditLine(mapper.mapFromEntity(creditLineComLoan))


    /**
     * Insert many credit line
     *
     * @param creditLineComLoanList
     */
    override suspend fun insertManyCreditLine(creditLineComLoanList: List<CreditLineComLoan>) =
        dao.insertManyCreditLine(mapper.mapListFromEntity(creditLineComLoanList))


    /**
     * Update credit line
     *
     * @param creditLineComLoan
     */
    override suspend fun updateCreditLine(creditLineComLoan: CreditLineComLoan) =
        dao.updateCreditLine(mapper.mapFromEntity(creditLineComLoan))


    /**
     * Update many credit lines
     *
     * @param creditLineComLoanList
     */
    override suspend fun updateManyCreditLines(creditLineComLoanList: List<CreditLineComLoan>) =
        dao.updateManyCreditLines(mapper.mapListFromEntity(creditLineComLoanList))


    /**
     * Delete credit line
     *
     * @param creditLineComLoan
     */
    override suspend fun deleteCreditLine(creditLineComLoan: CreditLineComLoan) =
        dao.deleteCreditLine(mapper.mapFromEntity(creditLineComLoan))


    /**
     * Delete many credit lines
     *
     * @param creditLineComLoanList
     */
    override suspend fun deleteManyCreditLines(creditLineComLoanList: List<CreditLineComLoan>) =
        dao.deleteManyCreditLines(mapper.mapListFromEntity(creditLineComLoanList))


    /**
     * Get all credit lines of the user
     *
     * @return
     */
    override fun getAllCreditLines() = dao.getAllCreditLines().map {
        mapper.mapListToEntity(it)
    }

    /**
     * Get current credit line of users
     *
     * @return
     */
    override fun getCurrentCreditLineOfUsers() = dao.getCurrentCreditLineOfUsers().map {
        mapper.mapListToEntity(it)
    }
}