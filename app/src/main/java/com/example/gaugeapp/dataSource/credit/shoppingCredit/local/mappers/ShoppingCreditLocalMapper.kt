package com.example.gaugeapp.dataSource.credit.shoppingCredit.local.mappers

import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities.ShoppingCreditTable
import com.example.gaugeapp.entities.ShoppingCredit

import com.example.gaugeapp.utils.EntityMapper
import java.util.*
import javax.inject.Inject

class ShoppingCreditLocalMapper @Inject constructor() :
    EntityMapper<ShoppingCreditTable, ShoppingCredit>() {

    override fun mapFromEntity(entity: ShoppingCredit): ShoppingCreditTable {

        return ShoppingCreditTable(
            shpCditUid = entity.uid,
            userUid = entity.userUid,
            idCreditLine = entity.idCreditLine,
            amount = entity.amount,
            _isSolved = entity._isSolved,
            createAt = entity.createAt ?: Date(),
            syncDate = entity.syncDate
        )
    }

    override fun mapToEntity(storageEntity: ShoppingCreditTable): ShoppingCredit {
        return ShoppingCredit(
            uid = storageEntity.shpCditUid,
            userUid = storageEntity.userUid,
            idCreditLine = storageEntity.idCreditLine,
            amount = storageEntity.amount,
            _isSolved = storageEntity._isSolved,
            createAt = storageEntity.createAt ?: Date(),
            syncDate = storageEntity.syncDate
        )
    }


}