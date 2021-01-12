package com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class ShoppingCreditTable(

    @ColumnInfo(name = "shpCditUid")
    @SerializedName("shpCditUid")
    @PrimaryKey(autoGenerate = false)
    var shpCditUid: String,

    @ColumnInfo(name = "userUid")
    @SerializedName("userUid")
    var userUid: String,

    @ColumnInfo(name = "idCreditLine")
    @SerializedName("idCreditLine")
    var idCreditLine: String,

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    var amount: Double,

    @ColumnInfo(name = "_isSolved")
    @SerializedName("_isSolved")
    val _isSolved: Boolean,

    @ColumnInfo(name = "createAt")
    @SerializedName("createAt")
    val createAt: Date,

    @ColumnInfo(name = "syncDate")
    @SerializedName("syncDate")
    val syncDate: Date
) {
}