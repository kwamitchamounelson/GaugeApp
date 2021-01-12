package com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities

import androidx.room.Embedded
import androidx.room.Relation

data class ShoppingCreditLineWithShoppingCreditsListPersist(
    @Embedded val shoppingCreditLineTable: ShoppingCreditLineTable,
    @Relation(
        parentColumn = "shpCditLineUid",
        entityColumn = "shpCditUid"
    )
    val creditTables: List<ShoppingCreditTable>
)