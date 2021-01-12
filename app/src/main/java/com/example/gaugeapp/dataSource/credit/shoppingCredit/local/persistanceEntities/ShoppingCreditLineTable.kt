package com.example.gaugeapp.dataSource.credit.shoppingCredit.local.persistanceEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class ShoppingCreditLineTable(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "shpCditLineUid")
    @SerializedName("shpCditLineUid")
    var shpCditLineUid: String,

    @ColumnInfo(name = "userId")
    @SerializedName("userId")
    var userId: String,

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    var amount: Double,

    @ColumnInfo(name = "dueDate")
    @SerializedName("dueDate")
    var dueDate: Date, //3 months by default

    @ColumnInfo(name = "payBackPercent")
    @SerializedName("payBackPercent")
    val payBackPercent: Double,

    @ColumnInfo(name = "minAmountToLoan")
    @SerializedName("minAmountToLoan")
    val minAmountToLoan: Double,

    @ColumnInfo(name = "createAt")
    @SerializedName("createAt")
    val createAt: Date,

    @ColumnInfo(name = "latestUpdateAt")
    @SerializedName("latestUpdateAt")
    val latestUpdateAt: Date
)