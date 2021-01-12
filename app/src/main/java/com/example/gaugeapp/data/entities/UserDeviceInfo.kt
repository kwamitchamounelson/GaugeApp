package com.example.gaugeapp.data.entities

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class UserDeviceInfo(
    var userUid: String,
    var versionName: String,
    var versionCode: Long,
    var deviceImeil: String,
    var deviceModel: String,
    var deviceName:String,
    var numberOfAcount: Int,
    var primaryAccount: String,
    @ServerTimestamp
    val savedOrUpdateAt: Date
) {
    var androidVersionSDK = 0
    constructor() : this("", "", 0, "", "", "", 0,"", Date())
}