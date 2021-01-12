package com.example.gaugeapp.ui.credit.airtimeCredit.main

import com.example.gaugeapp.entities.AirtimeCredit

/**
 * Airtime credit state event
 *
 * @constructor Create empty Airtime credit state event
 */
sealed class AirtimeCreditStateEvent {
    object GetCurrentAirtimeCreditLineOfTheUser : AirtimeCreditStateEvent()
    class InitAirtimeCreditLine(val airtimeCredit: AirtimeCredit) : AirtimeCreditStateEvent()
    class BorrowAirtimeCredit(
        val creditLineId: String,
        val amount: Double,
        val phoneNumber: String
    ) : AirtimeCreditStateEvent()
}