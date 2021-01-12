package com.example.gaugeapp.entities

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class ShoppingCredit(
    var uid: String,
    var userUid: String,
    var idCreditLine: String,
    var amount: Double,
    var _isSolved: Boolean,
    @ServerTimestamp
    val createAt: Date? = null,
    val syncDate: Date
) {

    var repaymentDate: Date = Date()

    constructor() : this(
        uid = "",
        userUid = "",
        idCreditLine = "",
        amount = 0.0,
        _isSolved = false,
        createAt = Date(),
        syncDate = Date()
    )
}