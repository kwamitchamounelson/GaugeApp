package com.example.gaugeapp.ui.credit.items

import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit_pending_indicator.view.*
import org.jetbrains.anko.backgroundResource

class PendingItem(var number: Int, var isCurrentPending: Boolean = false) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_airtime_credit_borrow_pending.apply {
            backgroundResource = if (isCurrentPending) {
                R.drawable.background_credit_pending_enable
            } else {
                R.drawable.background_credit_pending_denable
            }
        }
    }

    override fun getLayout() = R.layout.item_credit_pending_indicator
}