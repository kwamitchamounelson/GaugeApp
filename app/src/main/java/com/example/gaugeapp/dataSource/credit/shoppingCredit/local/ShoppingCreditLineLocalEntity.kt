package com.example.gaugeapp.dataSource.credit.shoppingCredit.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Shopping credit line local entity
 *
 * @property id
 * @property userId
 * @property maxAmountToLoan
 * @property dueDate
 * @property shoppingCreditListString
 * @property payBackPercent
 * @property minAmountToLoan
 * @property createAt
 * @property syncDate
 * @property solved
 * @constructor Create empty Shopping credit line local entity
 */
@Entity
data class ShoppingCreditLineLocalEntity(
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
     * represent serialized value of ShoppingCredit list
     */
    @ColumnInfo(name = "shoppingCreditListString")
    @SerializedName("shoppingCreditListString")
    val shoppingCreditListString: String,

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