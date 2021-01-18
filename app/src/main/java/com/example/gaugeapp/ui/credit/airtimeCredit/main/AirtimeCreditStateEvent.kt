package com.example.gaugeapp.ui.credit.airtimeCredit.main

import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.entities.AirTimeCreditLine

/**
 * Airtime credit state event
 *
 * @constructor Create empty Airtime credit state event
 */
sealed class AirtimeCreditStateEvent {
    object GetCurrentAirtimeCreditLineOfTheUser : AirtimeCreditStateEvent()

    class GetLastAirtimeCreditRequest(val currentCreditLineId: String) : AirtimeCreditStateEvent()

    object InitAirtimeCreditLine : AirtimeCreditStateEvent()

    class RequestBorrowAirtimeCredit(val airtimeCreditRequest: AirtimeCreditRequest) : AirtimeCreditStateEvent()

    class CloseValidatedAirtimeCreditRequest(val currentAirtimeCreditLine: AirTimeCreditLine, val currentAirtimeCreditRequest: AirtimeCreditRequest) : AirtimeCreditStateEvent()

    class CloseAirtimeCreditRequest(val currentAirtimeCreditRequest: AirtimeCreditRequest) : AirtimeCreditStateEvent()

    class CancelCloselAirtimeCreditRequest(val currentAirtimeCreditRequest: AirtimeCreditRequest) : AirtimeCreditStateEvent()

    class CloseCurentCreditLine(val currentAirtimeCreditLine: AirTimeCreditLine) : AirtimeCreditStateEvent()
}