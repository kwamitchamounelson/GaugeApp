package com.example.gaugeapp.dataSource.credit.shoppingCredit.local.mappers

import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditLineTable
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.EntityMapper
import java.util.*
import javax.inject.Inject

class ShoppingCreditLineLocalMapper @Inject constructor() :
    EntityMapper<ShoppingCreditLineTable, ShoppingCreditLine>() {

    override fun mapFromEntity(entity: ShoppingCreditLine): ShoppingCreditLineTable {

        return ShoppingCreditLineTable(
            shpCditLineUid = entity.uid,
            userId = entity.userId,
            amount = entity.amount,
            dueDate = entity.dueDate,
            payBackPercent = entity.payBackPercent,
            minAmountToLoan = entity.minAmountToLoan,
            createAt = entity.createAt ?: Date(),
            latestUpdateAt = entity.latestUpdateAt
        )
    }

    override fun mapToEntity(storageEntity: ShoppingCreditLineTable): ShoppingCreditLine {
        return ShoppingCreditLine(
            uid = storageEntity.shpCditLineUid,
            userId = storageEntity.userId,
            amount = storageEntity.amount,
            dueDate = storageEntity.dueDate,
            payBackPercent = storageEntity.payBackPercent,
            minAmountToLoan = storageEntity.minAmountToLoan,
            createAt = storageEntity.createAt,
            latestUpdateAt = storageEntity.latestUpdateAt
        )
    }


}