package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Airtime credit line local entity
 * We will not put the list of AirtimeCredit because we will make the relation with the AirtimeCredit table
 *
 * @author: Nelson
 *
 * @property id
 * @property userId
 * @property maxAmountToLoan
 * @property dueDate
 * @property payBackPercent
 * @property minAmountToLoan
 * @property createAt
 * @property syncDate
 * @constructor Create empty Airtime credit line local entity
 */
@Entity
data class AirtimesCreditLineLocalEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name = "userId")
    @SerializedName("userId")
    val userId: String,

    @ColumnInfo(name = "maxAmountToLoan")
    @SerializedName("maxAmountToLoan")
    val maxAmountToLoan: Double,

    @ColumnInfo(name = "dueDate")
    @SerializedName("dueDate")
    val dueDate: Long,


    /**
     * represent serialized value of AirtimeCredit list
     */
    @ColumnInfo(name = "airtimeCreditListString")
    @SerializedName("airtimeCreditListString")
    val airtimeCreditListString: String,

    @ColumnInfo(name = "payBackPercent")
    @SerializedName("payBackPercent")
    val payBackPercent: Double,

    @ColumnInfo(name = "minAmountToLoan")
    @SerializedName("minAmountToLoan")
    val minAmountToLoan: Double,

    @ColumnInfo(name = "createAt")
    @SerializedName("createAt")
    val createAt: Long,

    @ColumnInfo(name = "syncDate")
    @SerializedName("syncDate")
    val syncDate: Long,

    @ColumnInfo(name = "solved")
    @SerializedName("solved")
    var solved: Boolean
)