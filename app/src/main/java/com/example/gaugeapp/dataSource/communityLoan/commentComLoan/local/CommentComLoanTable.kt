package com.example.gaugeapp.dataSource.communityLoan.commentComLoan.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class CommentComLoanTable(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: String,

    @ColumnInfo(name = "campaignId")
    @SerializedName("campaignId")
    var campaignId: String,

    @ColumnInfo(name = "text")
    @SerializedName("text")
    var text: String,

    @ColumnInfo(name = "createAt")
    @SerializedName("createAt")
    var createAt: Long,

    @ColumnInfo(name = "parentId")
    @SerializedName("parentId")
    var parentId: String
)