package com.example.gaugeapp.entities

import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine

data class ShoppingCreditLineWithShoppingCreditsList(
    val shoppingCreditLine: ShoppingCreditLine,
    val creditList: List<ShoppingCredit>
) {
}