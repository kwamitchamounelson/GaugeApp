package com.example.gaugeapp.commonRepositories.roomRep.roomEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class SmsTable(
   @ColumnInfo(name = "id")
   @SerializedName("id")
   @PrimaryKey(autoGenerate = false)
   val id: String,

   @ColumnInfo(name = "date")
   @SerializedName("date")
   val date: Long,

   @ColumnInfo(name = "serviceOperateur")
   @SerializedName("serviceOperateur")
   val serviceOperateur: String,

   @ColumnInfo(name = "enumService")
   @SerializedName("enumService")
   val enumService:String,

   @ColumnInfo(name = "isDeleted")
   @SerializedName("isDeleted")
   val isDeleted:Boolean,

   @ColumnInfo(name = "cutumSmsString")
   @SerializedName("cutumSmsString")
   val cutumSmsString: String
) {
}