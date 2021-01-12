package com.example.gaugeapp.dataSource.user.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name= "userUuid")
    @SerializedName("userUuid")
    val userUuid: String,

    @ColumnInfo(name= "userName")
    @SerializedName("userName")
    val userName: String,

    @ColumnInfo(name= "fullName")
    @SerializedName("fullName")
    val fullName: String,

    @ColumnInfo(name= "email")
    @SerializedName("email")
    val email: String,

    @ColumnInfo(name= "imageUrl")
    @SerializedName("imageUrl")
    val imageUrl: String,

    @ColumnInfo(name= "userCreatedDate")
    @SerializedName("userCreatedDate")
    val userCreatedDate: Long,

    @ColumnInfo(name= "authPhoneNumber")
    @SerializedName("authPhoneNumber")
    val authPhoneNumber: String,

    @ColumnInfo(name= "mobileMoneyNumbers")
    @SerializedName("mobileMoneyNumbers")
    val mobileMoneyNumbers: String,

    @ColumnInfo(name= "phoneNumbers")
    @SerializedName("phoneNumbers")
    val phoneNumbers: String,

    @ColumnInfo(name= "detectedName")
    @SerializedName("detectedName")
    val detectedName: String
)















