package com.example.gaugeapp.ui.credit.airtimeCredit.history

import com.example.gaugeapp.entities.AirTimeCreditLine

sealed class AirtimeCreditHistoryStateEven {
    object GetDataFirstTime : AirtimeCreditHistoryStateEven()
    class GetDataAfterFirstTime(val lastCreditLine: AirTimeCreditLine) :
        AirtimeCreditHistoryStateEven()
}