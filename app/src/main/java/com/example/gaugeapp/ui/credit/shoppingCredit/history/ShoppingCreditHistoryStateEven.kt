package com.example.gaugeapp.ui.credit.shoppingCredit.history

import com.example.gaugeapp.entities.ShoppingCreditLine


sealed class ShoppingCreditHistoryStateEven {
    object GetDataFirstTime : ShoppingCreditHistoryStateEven()
    class GetDataAfterFirstTime(val lastCreditLine: ShoppingCreditLine) :
        ShoppingCreditHistoryStateEven()
}