package com.example.gaugeapp.dataSource.communityLoan.campaign.local

import androidx.room.*


import kotlinx.coroutines.flow.Flow


/**
 * Campaign Community loan dao
 *
 * contain all dao of community loan module
 *
 * @constructor Create empty Community loan dao
 */
@Dao
interface CampaignComLoanDao {

    //---------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------START DAO FOR CAMPAIGNS----------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaignComLoanTable: CampaignComLoanTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManyCampaign(campaignComLoanTableList: List<CampaignComLoanTable>)

    @Update
    suspend fun updateCampaign(campaignComLoanTable: CampaignComLoanTable)

    @Update
    suspend fun updateManyCampaigns(campaignComLoanTableList: List<CampaignComLoanTable>)

    @Delete
    suspend fun deleteCampaign(campaignComLoanTable: CampaignComLoanTable)

    @Delete
    suspend fun deleteManyCampaigns(campaignComLoanTableList: List<CampaignComLoanTable>)


    /**
     * Get all campaigns that are in the cache
     *
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable ORDER BY createAt DESC")
    fun getAllCampaigns(): Flow<List<CampaignComLoanTable>>


    /**
     * Get all campaigns limit
     *
     * @param limit
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable ORDER BY createAt DESC LIMIT :limit")
    fun getAllCampaignsLimit(limit: Int): Flow<List<CampaignComLoanTable>>


    /**
     * Get all actives campaigns
     *
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable WHERE enable = 1 ORDER BY createAt DESC")
    fun getAllActivesCampaigns(): Flow<List<CampaignComLoanTable>>


    /**
     * Get all actives campaigns limit
     *
     * @param limit
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable WHERE enable = 1 ORDER BY createAt DESC LIMIT :limit")
    fun getAllActivesCampaignsLimit(limit: Int): Flow<List<CampaignComLoanTable>>


    /**
     * Get all active campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable WHERE enable = 1 AND ownerId=:userId AND creditLineId=:creditLineId ORDER BY createAt DESC")
    fun getAllActiveCampaignsOfUser(
        userId: String,
        creditLineId: String
    ): Flow<List<CampaignComLoanTable>>

    /**
     * Get all campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable WHERE ownerId=:userId AND creditLineId=:creditLineId ORDER BY createAt DESC")
    fun getAllCampaignsOfUser(
        userId: String,
        creditLineId: String
    ): Flow<List<CampaignComLoanTable>>


    /**
     * Get all un solved campaigns of user
     *
     * @param userId
     * @param creditLineId
     * @return
     */
    @Query("SELECT * FROM campaigncomloantable WHERE solved=0 AND ownerId=:userId AND creditLineId=:creditLineId ORDER BY createAt DESC")
    fun getAllUnSolvedCampaignsOfUser(
        userId: String,
        creditLineId: String
    ): Flow<List<CampaignComLoanTable>>

    /*@Transaction
    @Query("SELECT * FROM campaigncomloantable WHERE id =:campaignId")
    fun getCampaignWithComments(campaignId: String): Flow<CampaignWithCommentTable>*/
    //---------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------END DAO FOR CAMPAIGNS----------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------

}