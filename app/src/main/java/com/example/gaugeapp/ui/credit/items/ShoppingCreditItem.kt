package com.example.gaugeapp.ui.credit.items

import android.view.View
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.extentions.convertDateToASpecificFormatWithTodayAndYesterdayInCount
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit.view.*
import org.jetbrains.anko.textColorResource
import java.util.*

class ShoppingCreditItem(
    var shoppingCredit: ShoppingCredit,
    var shoppingCreditLine: ShoppingCreditLine
) : Item() {
    private val nowDate = Calendar.getInstance().time
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.id_credit_amoun_to_pay_back.text = getTotalAmount()

        viewHolder.itemView.id_credit_due_date.apply {
            text =
                shoppingCreditLine.dueDate.convertDateToASpecificFormatWithTodayAndYesterdayInCount(
                    viewHolder.itemView.context,
                    format = "dd MMM",
                    isDayOfWeek = true,
                    withHour = false
                )

            textColorResource = if ((shoppingCreditLine.dueDate < nowDate)) {
                R.color.color_red_credit_due
            } else {
                R.color.colorPrimary
            }
        }

        viewHolder.itemView.id_credit_store_info.apply {
            visibility = View.VISIBLE
            //TODO show name of the store
            //text = shoppingCredit.storeName
        }

        viewHolder.itemView.id_credit_real_amount.text =
            shoppingCredit.amount.formatNumberWithSpaceBetweenThousand()
    }

    private fun getTotalAmount(): String {
        val totalAmount = (shoppingCredit.amount * (1 + shoppingCreditLine.payBackPercent))
        return totalAmount.formatNumberWithSpaceBetweenThousand() + " F"
    }

    override fun getLayout() = R.layout.item_credit
}