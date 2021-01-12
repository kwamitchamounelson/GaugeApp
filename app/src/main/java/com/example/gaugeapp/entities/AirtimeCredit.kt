package com.example.gaugeapp.entities

import java.io.Serializable
import java.util.*


data class AirtimeCredit(var id: String) : Serializable {
    var idCreditLine: String = ""
    var amount: Double = 0.0
    var solved: Boolean = false
    var dateOfLoan: Date = Date()
    var repaymentDate: Date = Date()

    constructor() : this("")
}