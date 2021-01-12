package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local

import com.example.gaugeapp.entities.AirTimeCreditLine

interface AirtimeCreditLineLocalDataSource {

    /**
     * Insert airtime credit line
     *
     * @param airtimeCreditLine
     */
    suspend fun insertAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine)

    /**
     * Insert many airtime credit line
     *
     * @param airtimeCreditLineList
     */
    suspend fun insertManyAirtimeCreditLine(airtimeCreditLineList: List<AirTimeCreditLine>)

    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     */
    suspend fun updateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine)

    /**
     * Update many airtime credit line
     *
     * @param airtimeCreditLine
     */
    suspend fun updateManyAirtimeCreditLine(airtimeCreditLine: List<AirTimeCreditLine>)

    /**
     * Delete airtime credit line
     *
     * @param airtimeCreditLine
     */
    suspend fun deleteAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine)

    /**
     * Delete many airtime credit line
     *
     * @param airtimeCreditLine
     */
    suspend fun deleteManyAirtimeCreditLine(airtimeCreditLine: List<AirTimeCreditLine>)

    /**
     * Get all airtime credit line
     *
     * @return
     */
    suspend fun getAllAirtimeCreditLine(): List<AirTimeCreditLine>
}