package com.example.gaugeapp.ui.credit.items

import com.example.gaugeapp.R
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.entities.AirtimeCredit
import com.example.gaugeapp.utils.extentions.convertDateToASpecificFormatWithTodayAndYesterdayInCount
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit.view.*
import org.jetbrains.anko.textColorResource
import java.util.*

class AirtimeCreditItem(
    var airtimeCredit: AirtimeCredit,
    var airTimeCreditLine: AirTimeCreditLine
) : Item() {
    private val nowDate = Calendar.getInstance().time
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.id_credit_amoun_to_pay_back.text = getTotalAmount()

        viewHolder.itemView.id_credit_due_date.apply {
            text =
                airTimeCreditLine.dueDate.convertDateToASpecificFormatWithTodayAndYesterdayInCount(
                    viewHolder.itemView.context,
                    format = "dd MMM",
                    isDayOfWeek = true,
                    withHour = false
                )

            textColorResource = if ((airTimeCreditLine.dueDate < nowDate)) {
                R.color.color_red_credit_due
            } else {
                R.color.colorPrimary
            }
        }

        viewHolder.itemView.id_credit_real_amount.text =
            airtimeCredit.amount.formatNumberWithSpaceBetweenThousand()
    }

    private fun getTotalAmount(): String {
        val totalamount = (airtimeCredit.amount * (1 + airTimeCreditLine.payBackPercent))
        return totalamount.formatNumberWithSpaceBetweenThousand() + " F"
    }

    override fun getLayout() = R.layout.item_credit
}