package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local

import com.example.gaugeapp.entities.AirTimeCreditLine
import javax.inject.Inject

class AirtimeCreditLineLocalDataSourceImpl @Inject constructor(
    private val mapper: AirtimesCreditLineLocalMapper,
    private val dao: AirtimesCreditLineDao
) : AirtimeCreditLineLocalDataSource {


    /**
     * Insert airtime credit line
     *
     * @param airtimeCreditLine
     */
    override suspend fun insertAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) =
        dao.insertAirtimeCreditLine(mapper.mapFromEntity(airtimeCreditLine))


    /**
     * Insert many airtime credit line
     *
     * @param airtimeCreditLineList
     */
    override suspend fun insertManyAirtimeCreditLine(airtimeCreditLineList: List<AirTimeCreditLine>) =
        dao.insertAllAirtimeCreditLine(mapper.mapListFromEntity(airtimeCreditLineList))


    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     */
    override suspend fun updateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) =
        dao.updateAirtimeCreditLine(mapper.mapFromEntity(airtimeCreditLine))

    /**
     * Update many airtime credit line
     *
     * @param airtimeCreditLine
     */
    override suspend fun updateManyAirtimeCreditLine(airtimeCreditLine: List<AirTimeCreditLine>) =
        dao.updateAllAirtimeCreditLine(mapper.mapListFromEntity(airtimeCreditLine))


    /**
     * Delete airtime credit line
     *
     * @param airtimeCreditLine
     */
    override suspend fun deleteAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) =
        dao.deleteAirtimeCreditLine(mapper.mapFromEntity(airtimeCreditLine))


    /**
     * Delete many airtime credit line
     *
     * @param airtimeCreditLine
     */
    override suspend fun deleteManyAirtimeCreditLine(airtimeCreditLine: List<AirTimeCreditLine>) =
        dao.deleteAllAirtimeCreditLine(mapper.mapListFromEntity(airtimeCreditLine))

    /**
     * Get all airtime credit line
     *
     * @return
     */
    override suspend fun getAllAirtimeCreditLine(): List<AirTimeCreditLine> =
        mapper.mapListToEntity(dao.getAllAirtimeCreditLine())

}