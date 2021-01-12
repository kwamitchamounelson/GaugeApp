package com.example.gaugeapp.dataSource.communityLoan.creditLine.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * Credit line com loan table
 *
 * @property id
 * @property repaymentDate
 * @property createDate
 * @property ownerId
 * @property solved
 * @constructor Create empty Credit line com loan table
 */
@Entity
data class CreditLineComLoanTable(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "repaymentDate")
    @SerializedName("repaymentDate")
    var repaymentDate: Long,

    @ColumnInfo(name = "createDate")
    @SerializedName("createDate")
    var createDate: Long,

    @ColumnInfo(name = "ownerId")
    @SerializedName("ownerId")
    var ownerId: String,

    @ColumnInfo(name = "solved")
    @SerializedName("solved")
    var solved: Boolean = false
)