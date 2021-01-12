package com.example.gaugeapp.dataSource.communityLoan.campaign.local

import com.example.gaugeapp.entities.communityLoan.CampaignComLoan
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Campaign Community loan local data source implementation
 *
 * @property mapper
 * @property dao
 * @constructor Create empty Community loan local data source impl
 */
class CampaignComLoanLocalDataSourceImpl @Inject constructor(
    private val mapper: CampaignComLoanLocalMapper,
    private val dao: CampaignComLoanDao
) : CampaignComLoanLocalDataSource {
    //---------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------START DAO FOR CAMPAIGNS----------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------

    /**
     * Insert campaign
     *
     * @param campaignComLoan
     */
    override suspend fun insertCampaign(campaignComLoan: CampaignComLoan) =
        dao.insertCampaign(mapper.mapFromEntity(campaignComLoan))


    /**
     * Insert many campaign
     *
     * @param campaignComLoanList
     */
    override suspend fun insertManyCampaign(campaignComLoanList: List<CampaignComLoan>) =
        dao.insertManyCampaign(mapper.mapListFromEntity(campaignComLoanList))


    /**
     * Update campaign
     *
     * @param campaignComLoan
     */
    override suspend fun updateCampaign(campaignComLoan: CampaignComLoan) =
        dao.updateCampaign(mapper.mapFromEntity(campaignComLoan))


    /**
     * Update many campaigns
     *
     * @param campaignComLoanList
     */
    override suspend fun updateManyCampaigns(campaignComLoanList: List<CampaignComLoan>) =
        dao.updateManyCampaigns(mapper.mapListFromEntity(campaignComLoanList))


    /**
     * Delete campaign
     *
     * @param campaignComLoan
     */
    override suspend fun deleteCampaign(campaignComLoan: CampaignComLoan) =
        dao.deleteCampaign(mapper.mapFromEntity(campaignComLoan))


    /**
     * Delete many campaigns
     *
     * @param campaignComLoanList
     */
    override suspend fun deleteManyCampaigns(campaignComLoanList: List<CampaignComLoan>) =
        dao.deleteManyCampaigns(mapper.mapListFromEntity(campaignComLoanList))


    /**
     * Get all campaigns that are in the cache
     *
     * @return
     */
    override fun getAllCampaigns() = dao.getAllCampaigns().map {
        mapper.mapListToEntity(it)
    }


    /**
     * Get all campaigns limit
     *
     * @param limit
     * @return
     */
    override fun getAllCampaignsLimit(limit: Int) = dao.getAllCampaignsLimit(limit).map {
        mapper.mapListToEntity(it)
    }


    /**
     * Get all actives campaigns
     *
     * @return
     */
    override fun getAllActivesCampaigns() = dao.getAllActivesCampaigns().map {
        mapper.mapListToEntity(it)
    }


    /**
     * Get all actives campaigns limit
     *
     * @param limit
     * @return
     */
    override fun getAllActivesCampaignsLimit(limit: Int) =
        dao.getAllActivesCampaignsLimit(limit).map {
            mapper.mapListToEntity(it)
        }


    /**
     * Get all active campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    override fun getAllActiveCampaignsOfUser(
        userId: String,
        creditLineId: String
    ) = dao.getAllActiveCampaignsOfUser(userId, creditLineId).map {
        mapper.mapListToEntity(it)
    }

    /**
     * Get all campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    override fun getAllCampaignsOfUser(
        userId: String,
        creditLineId: String
    ) = dao.getAllCampaignsOfUser(userId, creditLineId).map {
        mapper.mapListToEntity(it)
    }


    /**
     * Get all un solved campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    override fun getAllUnSolvedCampaignsOfUser(
        userId: String,
        creditLineId: String
    ) = dao.getAllUnSolvedCampaignsOfUser(userId, creditLineId).map {
        mapper.mapListToEntity(it)
    }


    //---------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------END DAO FOR CAMPAIGNS----------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------
}