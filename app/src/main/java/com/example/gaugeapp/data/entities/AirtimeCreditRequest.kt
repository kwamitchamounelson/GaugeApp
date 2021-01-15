package com.example.gaugeapp.data.entities

import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import java.util.*

data class AirtimeCreditRequest(
    var id: String,
    var airtimeCreditLineId: String,
    var amount: Double,
    var phoneNumber: String,
    var userId: String,
    var creationDate: Date,
    var status: ENUM_REQUEST_STATUS,
    var lastUpdatedDate: Date,
    var enable: Boolean
) {
    constructor() : this("", "", 0.0, "", "", Date(), ENUM_REQUEST_STATUS.PENDING, Date(), false)

    var reasonOfReject = ""
}