package com.example.gaugeapp.dataSource.communityLoan.campaign.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Campaign com loan table
 *
 * @property id
 * @property creditLineId
 * @property ownerId
 * @property description
 * @property payBackDate
 * @property amount
 * @property reason
 * @property percentInterest
 * @property contributionsListString serialised value of list
 * @property contributorIdListString serialised value of list
 * @property guarantorsListString serialised value of list
 * @property guarantorIdListString serialised value of list
 * @property payBackListString serialised value of list
 * @property createAt
 * @property synDate
 * @property enable
 * @property solved
 * @property commentCount
 * @constructor Create empty Campaign com loan table
 */
@Entity
data class CampaignComLoanTable(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "creditLineId")
    @SerializedName("creditLineId")
    var creditLineId: String,

    @ColumnInfo(name = "ownerId")
    @SerializedName("ownerId")
    var ownerId: String,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    var description: String,

    @ColumnInfo(name = "payBackDate")
    @SerializedName("payBackDate")
    var payBackDate: Long,

    @ColumnInfo(name = "amount")
    @SerializedName("amount")
    var amount: Double,

    @ColumnInfo(name = "reason")
    @SerializedName("reason")
    var reason: String,

    @ColumnInfo(name = "percentInterest")
    @SerializedName("percentInterest")
    var percentInterest: Double,


    @ColumnInfo(name = "contributionsListString")
    @SerializedName("contributionsListString")
    var contributionsListString: String,


    @ColumnInfo(name = "contributorIdListString")
    @SerializedName("contributorIdListString")
    var contributorIdListString: String,

    @ColumnInfo(name = "guarantorsListString")
    @SerializedName("guarantorsListString")
    var guarantorsListString: String,

    @ColumnInfo(name = "guarantorIdListString")
    @SerializedName("guarantorIdListString")
    var guarantorIdListString: String,

    @ColumnInfo(name = "payBackListString")
    @SerializedName("payBackListString")
    var payBackListString: String,

    @ColumnInfo(name = "createAt")
    @SerializedName("createAt")
    var createAt: Long,

    @ColumnInfo(name = "synDate")
    @SerializedName("synDate")
    var synDate: Long,

    @ColumnInfo(name = "enable")
    @SerializedName("enable")
    var enable: Boolean,

    @ColumnInfo(name = "solved")
    @SerializedName("solved")
    var solved: Boolean,

    @ColumnInfo(name = "commentCount")
    @SerializedName("commentCount")
    var commentCount: Int
)