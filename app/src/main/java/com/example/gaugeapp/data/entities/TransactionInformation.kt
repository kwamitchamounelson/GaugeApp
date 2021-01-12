package com.example.gaugeapp.data.entities

import com.example.gaugeapp.data.entities.Cashflow
import com.kola.smsmodule.enums.ENUM_OPERATEUR
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import com.kola.smsmodule.enums.ENUM_SERVICE_OPERATEUR
import java.io.Serializable

class TransactionInformation(
    var serviceType: ENUM_SERVICE_ANALYSIS?,
    var amount: Double,
    var receiverPhoneNumber: String
) : Serializable {
    var senderOperator: ENUM_SERVICE_OPERATEUR? = null
    var userName: String = ""
    var userImage: String = ""
    var cashFlow: Cashflow? = null
    var free: Double = 0.0

    constructor() : this(null, 0.0, "")
}