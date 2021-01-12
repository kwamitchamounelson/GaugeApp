package com.example.gaugeapp.dataSource.communityLoan.campaign.remote

import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import com.example.gaugeapp.entities.communityLoan.CampaignComLoan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

/**
 * Campaign com loan remote data source impl
 *
 * @property service
 * @constructor Create empty Campaign com loan remote data source impl
 */
@ExperimentalCoroutinesApi
class CampaignComLoanRemoteDataSourceImpl @Inject constructor(
    private val service: CampaignComLoanRemoteService
) : BaseRemoteDataSource(), CampaignComLoanRemoteDataSource {

    /**
     * Create campaign com loan
     *
     * @param campaignComLoan
     * @return
     */
    override fun createCampaignComLoan(campaignComLoan: CampaignComLoan) =
        getResult { service.createCampaignComLoan(campaignComLoan) }


    /**
     * Update campaign com loan
     *
     * @param campaignComLoan
     * @return
     */
    override fun UpdateCampaignComLoan(campaignComLoan: CampaignComLoan) =
        getResult { service.UpdateCampaignComLoan(campaignComLoan) }


    /**
     * Get all campaign com loan
     *
     * @return
     */
    override fun getAllCampaignComLoan() =
        getResult { service.getAllCampaignComLoan() }


    /**
     * Get all active campaign com loan
     *
     * @return
     */
    override fun getAllActiveCampaignComLoan() =
        getResult { service.getAllActiveCampaignComLoan() }


    /**
     * Get campaign start at syn date real time
     *
     * @param syncDate
     * @return
     */
    override fun getCampaignStartAtSynDateRealTime(syncDate: Date) =
        getResult { service.getCampaignStartAtSynDateRealTime(syncDate) }


    /**
     * Get all un solved campaign of user
     *
     * @param userId
     * @return
     */
    override fun getAllUnSolvedCampaignOfUser(userId: String) =
        getResult { service.getAllUnSolvedCampaignOfUser(userId) }
}