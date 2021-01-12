package com.example.gaugeapp.ui.credit.shoppingCredit.main

import com.example.gaugeapp.entities.ShoppingCredit

sealed class ShoppingCreditStateEvent {
    object GetCurrentShoppingCreditLineOfTheUser : ShoppingCreditStateEvent()
    class InitShoppingCreditLine(val shoppingCredit: ShoppingCredit) : ShoppingCreditStateEvent()
    class BorrowShoppingCredit(
        val creditLineId: String,
        val amount: Double,
        val phoneNumber: String
    ) : ShoppingCreditStateEvent()
}