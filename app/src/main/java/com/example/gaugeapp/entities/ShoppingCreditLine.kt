package com.example.gaugeapp.entities

import com.example.gaugeapp.ui.credit.ConstantCredit.INTEREST_RATE
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class ShoppingCreditLine(
    var uid: String,
    var userId: String,
    var amount: Double,
    var dueDate: Date, //3 months by default
    val payBackPercent: Double,
    val minAmountToLoan: Double,
    @ServerTimestamp
    val createAt: Date? = null,
    val latestUpdateAt: Date
) {
    constructor() : this(
        uid = "",
        userId = "",
        amount = 15_000.0,
        dueDate = Date(),
        payBackPercent = INTEREST_RATE,
        minAmountToLoan = 500.0,
        createAt = Date(),
        latestUpdateAt = Date()
    )
}