package com.example.gaugeapp.dataSource.communityLoan.campaign.local

import com.example.gaugeapp.entities.communityLoan.CampaignComLoan
import kotlinx.coroutines.flow.Flow


/**
 * Campaign Community loan local data source
 *
 * @constructor Create empty Community loan local data source
 */
interface CampaignComLoanLocalDataSource {

    //---------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------START DAO FOR CAMPAIGNS----------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------
    /**
     * Insert campaign
     *
     * @param campaignComLoan
     */
    suspend fun insertCampaign(campaignComLoan: CampaignComLoan)

    /**
     * Insert many campaign
     *
     * @param campaignComLoanList
     */
    suspend fun insertManyCampaign(campaignComLoanList: List<CampaignComLoan>)

    /**
     * Update campaign
     *
     * @param campaignComLoan
     */
    suspend fun updateCampaign(campaignComLoan: CampaignComLoan)

    /**
     * Update many campaigns
     *
     * @param campaignComLoanList
     */
    suspend fun updateManyCampaigns(campaignComLoanList: List<CampaignComLoan>)

    /**
     * Delete campaign
     *
     * @param campaignComLoan
     */
    suspend fun deleteCampaign(campaignComLoan: CampaignComLoan)

    /**
     * Delete many campaigns
     *
     * @param campaignComLoanList
     */
    suspend fun deleteManyCampaigns(campaignComLoanList: List<CampaignComLoan>)

    /**
     * Get all campaigns that are in the cache
     *
     * @return
     */
    fun getAllCampaigns(): Flow<List<CampaignComLoan>>

    /**
     * Get all campaigns limit
     *
     * @param limit
     * @return
     */
    fun getAllCampaignsLimit(limit: Int): Flow<List<CampaignComLoan>>

    /**
     * Get all actives campaigns
     *
     * @return
     */
    fun getAllActivesCampaigns(): Flow<List<CampaignComLoan>>

    /**
     * Get all actives campaigns limit
     *
     * @param limit
     * @return
     */
    fun getAllActivesCampaignsLimit(limit: Int): Flow<List<CampaignComLoan>>

    /**
     * Get all active campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    fun getAllActiveCampaignsOfUser(
        userId: String,
        creditLineId: String
    ): Flow<List<CampaignComLoan>>

    /**
     * Get all campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    fun getAllCampaignsOfUser(
        userId: String,
        creditLineId: String
    ): Flow<List<CampaignComLoan>>

    /**
     * Get all un solved campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    fun getAllUnSolvedCampaignsOfUser(
        userId: String,
        creditLineId: String
    ): Flow<List<CampaignComLoan>>

    //---------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------END DAO FOR CAMPAIGNS----------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------
}