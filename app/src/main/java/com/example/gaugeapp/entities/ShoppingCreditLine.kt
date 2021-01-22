package com.example.gaugeapp.entities

import com.example.gaugeapp.ui.credit.ConstantCredit.INTEREST_RATE
import java.io.Serializable
import java.util.*

data class ShoppingCreditLine(var id: String) : Serializable {
    var userId: String = ""
    var maxAmountToLoan: Double = 15000.0 // 15000 FCFA
    var dueDate: Date = Date()
    var shoppingCreditList: List<ShoppingCredit> = listOf() // pour eviter les lectures
    var payBackPercent: Double = INTEREST_RATE //10%
    var minAmountToLoan: Double = 500.0
    var createAt: Date = Date()
    var syncDate: Date = Date()
    var solved: Boolean = false

    constructor() : this("")
}