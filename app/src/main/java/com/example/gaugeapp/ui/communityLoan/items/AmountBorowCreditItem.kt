package com.example.gaugeapp.ui.communityLoan.items

import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_amount_borrow_credit.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColorResource

class AmountBorowCreditItem(var amount: Int, var itemSelected: Boolean = false) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_credit_amount.apply {
            text = amount.toString()
            if (itemSelected) {
                backgroundResource = R.drawable.background_amount_credit_selected
                textColorResource = R.color.colorPrimary
            } else {
                backgroundResource = R.drawable.background_amount_credit
                textColorResource = R.color.color_amount_credit
            }
        }
    }

    override fun getLayout() = R.layout.item_amount_borrow_credit
}