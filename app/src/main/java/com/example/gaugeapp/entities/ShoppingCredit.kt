package com.example.gaugeapp.entities

import java.io.Serializable
import java.util.*

data class ShoppingCredit(var id: String) : Serializable {
    var idCreditLine: String = ""
    var amount: Double = 0.0
    var solved: Boolean = false
    var store: Store = Store()
    var dateOfLoan: Date = Date()
    var repaymentDate: Date = Date()

    constructor() : this("")
}