package com.example.gaugeapp.ui.credit.items

import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.extentions.convertDateToASpecificFormatWithTodayAndYesterdayInCount
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit_history.view.*
import org.jetbrains.anko.textResource

class ShoppingCreditHistoryItem(
    var shoppingCredit: ShoppingCredit,
    var shoppingCreditLine: ShoppingCreditLine
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        //amount + percentage
        viewHolder.itemView.id_credit_history_amount_to_pay_back.text = getTotalAmount()

        //real amount
        viewHolder.itemView.id_credit_history_real_amount.text =
            shoppingCredit.amount.formatNumberWithSpaceBetweenThousand()

        //paid on
        viewHolder.itemView.id_credit_history_paid_on_date.text =
            shoppingCredit.repaymentDate.convertDateToASpecificFormatWithTodayAndYesterdayInCount(
                viewHolder.itemView.context,
                format = "dd MMM",
                isDayOfWeek = true,
                withHour = false
            )

        //due date
        viewHolder.itemView.id_credit_history_real_due_date.text =
            shoppingCreditLine.dueDate.convertDateToASpecificFormatWithTodayAndYesterdayInCount(
                viewHolder.itemView.context,
                format = "dd MMM",
                isDayOfWeek = true,
                withHour = false
            )

        //status of repayment
        viewHolder.itemView.id_credit_history_pay_back_status.textResource =
            if (shoppingCredit.repaymentDate <= shoppingCreditLine.dueDate) {
                R.string.on_time
            } else {
                R.string.late
            }

    }

    private fun getTotalAmount(): String {
        val totalAmount = (shoppingCredit.amount * (1 + shoppingCreditLine.payBackPercent))
        return totalAmount.formatNumberWithSpaceBetweenThousand() + " F"
    }

    override fun getLayout() = R.layout.item_credit_history
}