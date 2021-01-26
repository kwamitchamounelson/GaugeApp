package com.example.gaugeapp.ui.credit.shoppingCredit.main

import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.entities.ShoppingCreditLine

sealed class ShoppingCreditStateEvent {

    object GetCurrentShoppingCreditLineOfTheUser : ShoppingCreditStateEvent()

    class GetLastShoppingCreditRequest(val currentCreditLineId: String) : ShoppingCreditStateEvent()

    object InitShoppingCreditLine : ShoppingCreditStateEvent()

    class RequestBorrowShoppingCredit(val shoppingCreditRequest: ShoppingCreditRequest) : ShoppingCreditStateEvent()

    class CloseValidatedShoppingCreditRequest(val currentShoppingCreditLine: ShoppingCreditLine, val currentShoppingCreditRequest: ShoppingCreditRequest) : ShoppingCreditStateEvent()

    class CloseShoppingCreditRequest(val currentShoppingCreditRequest: ShoppingCreditRequest) : ShoppingCreditStateEvent()

    class CancelCloselShoppingCreditRequest(val currentShoppingCreditRequest: ShoppingCreditRequest) : ShoppingCreditStateEvent()

    class CloseCurentCreditLine(val currentShoppingCreditLine: ShoppingCreditLine) : ShoppingCreditStateEvent()

    object GetStoreList : ShoppingCreditStateEvent()
}