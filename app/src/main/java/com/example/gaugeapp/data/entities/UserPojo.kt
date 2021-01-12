package com.example.gaugeapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI
@Entity
data class UserPojo(@PrimaryKey var userId:String, var userName:String?, val userImage:String?, val userLocalImage: String?, var userPhoneNumber:String? ){
constructor():this("","","","","")
}