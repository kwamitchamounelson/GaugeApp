package com.example.gaugeapp.dataSource.communityLoan.campaign.remote

import com.example.gaugeapp.entities.communityLoan.CampaignComLoan
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Campaign com loan remote data source
 *
 * @constructor Create empty Campaign com loan remote data source
 */
interface CampaignComLoanRemoteDataSource {
    /**
     * Create campaign com loan
     *
     * @param campaignComLoan
     * @return
     */
    fun createCampaignComLoan(campaignComLoan: CampaignComLoan): Flow<DataState<CampaignComLoan>>

    /**
     * Update campaign com loan
     *
     * @param campaignComLoan
     * @return
     */
    fun UpdateCampaignComLoan(campaignComLoan: CampaignComLoan): Flow<DataState<CampaignComLoan>>

    /**
     * Get all campaign com loan
     *
     * @return
     */
    fun getAllCampaignComLoan(): Flow<DataState<List<CampaignComLoan>>>

    /**
     * Get all active campaign com loan
     *
     * @return
     */
    fun getAllActiveCampaignComLoan(): Flow<DataState<List<CampaignComLoan>>>

    /**
     * Get campaign start at syn date real time
     *
     * @param syncDate
     * @return
     */
    fun getCampaignStartAtSynDateRealTime(syncDate: Date): Flow<DataState<List<CampaignComLoan>>>

    /**
     * Get all un solved campaign of user
     *
     * @param userId
     * @return
     */
    fun getAllUnSolvedCampaignOfUser(userId: String): Flow<DataState<List<CampaignComLoan>>>
}