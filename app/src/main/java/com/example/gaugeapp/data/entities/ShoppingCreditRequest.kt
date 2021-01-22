package com.example.gaugeapp.data.entities

import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import com.example.gaugeapp.entities.Store
import java.util.*

data class ShoppingCreditRequest(
    var id: String,
    var shoppingCreditLineId: String,
    var amount: Double,
    var store: Store,
    var userId: String,
    var creationDate: Date,
    var status: ENUM_REQUEST_STATUS,
    var lastUpdatedDate: Date,
    var requestEnable: Boolean
) {
    constructor() : this("", "", 0.0, Store(), "", Date(), ENUM_REQUEST_STATUS.PENDING, Date(), false)

    var reasonOfReject = ""
}