package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.EntityMapper
import com.example.gaugeapp.utils.fromLong
import com.example.gaugeapp.utils.toLong
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject


/**
 * Shopping credit line local mapper
 *
 * @constructor Create empty Shopping credit line local mapper
 */
class ShoppingCreditLineLocalMapper @Inject constructor() :
    EntityMapper<ShoppingCreditLineLocalEntity, ShoppingCreditLine>() {
    override fun mapFromEntity(entity: ShoppingCreditLine): ShoppingCreditLineLocalEntity {
        return ShoppingCreditLineLocalEntity(
            id = entity.id,
            userId = entity.userId,
            maxAmountToLoan = entity.maxAmountToLoan,
            dueDate = entity.dueDate.toLong(),
            payBackPercent = entity.payBackPercent,
            minAmountToLoan = entity.minAmountToLoan,
            createAt = entity.createAt.toLong(),
            syncDate = entity.syncDate.toLong(),
            shoppingCreditListString = serializeList(entity.shoppingCreditList),
            solved = entity.solved
        )
    }

    override fun mapToEntity(storageEntity: ShoppingCreditLineLocalEntity): ShoppingCreditLine {
        return ShoppingCreditLine().apply {
            id = storageEntity.id
            userId = storageEntity.userId
            maxAmountToLoan = storageEntity.maxAmountToLoan
            dueDate = storageEntity.dueDate.fromLong()
            payBackPercent = storageEntity.payBackPercent
            minAmountToLoan = storageEntity.minAmountToLoan
            createAt = storageEntity.createAt.fromLong()
            syncDate = storageEntity.syncDate.fromLong()
            shoppingCreditList = deSerializeString(storageEntity.shoppingCreditListString)
            solved = storageEntity.solved
        }
    }


    private fun serializeList(list: List<ShoppingCredit>): String {
        return Gson().toJson(list)
    }


    private fun deSerializeString(listStr: String): List<ShoppingCredit> {
        val sType = object : TypeToken<List<ShoppingCredit>>() {}.type
        return Gson().fromJson<List<ShoppingCredit>>(listStr, sType)
    }

}