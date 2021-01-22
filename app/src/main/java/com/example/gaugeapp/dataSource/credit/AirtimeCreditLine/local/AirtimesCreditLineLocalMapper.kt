package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local

import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.entities.AirtimeCredit
import com.example.gaugeapp.utils.EntityMapper
import com.example.gaugeapp.utils.fromLong
import com.example.gaugeapp.utils.toLong
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject


/**
 * Airtime credit line local mapper
 *
 * @constructor Create empty Airtime credit line local mapper
 */
class AirtimesCreditLineLocalMapper @Inject constructor() :
    EntityMapper<AirtimesCreditLineLocalEntity, AirTimeCreditLine>() {
    override fun mapFromEntity(entity: AirTimeCreditLine): AirtimesCreditLineLocalEntity {
        return AirtimesCreditLineLocalEntity(
            id = entity.id,
            userId = entity.userId,
            maxAmountToLoan = entity.maxAmountToLoan,
            dueDate = entity.dueDate.toLong(),
            payBackPercent = entity.payBackPercent,
            minAmountToLoan = entity.minAmountToLoan,
            createAt = entity.createAt.toLong(),
            syncDate = entity.syncDate.toLong(),
            airtimeCreditListString = serializeList(entity.airtimeCreditList),
            solved = entity.solved
        )
    }

    override fun mapToEntity(storageEntity: AirtimesCreditLineLocalEntity): AirTimeCreditLine {
        return AirTimeCreditLine().apply {
            id = storageEntity.id
            userId = storageEntity.userId
            maxAmountToLoan = storageEntity.maxAmountToLoan
            dueDate = storageEntity.dueDate.fromLong()
            payBackPercent = storageEntity.payBackPercent
            minAmountToLoan = storageEntity.minAmountToLoan
            createAt = storageEntity.createAt.fromLong()
            syncDate = storageEntity.syncDate.fromLong()
            airtimeCreditList = deSerializeString(storageEntity.airtimeCreditListString)
            solved = storageEntity.solved
        }
    }


    private fun serializeList(list: List<AirtimeCredit>): String {
        return Gson().toJson(list)
    }


    private fun deSerializeString(listStr: String): List<AirtimeCredit> {
        val sType = object : TypeToken<List<AirtimeCredit>>() {}.type
        return Gson().fromJson<List<AirtimeCredit>>(listStr, sType)
    }

}