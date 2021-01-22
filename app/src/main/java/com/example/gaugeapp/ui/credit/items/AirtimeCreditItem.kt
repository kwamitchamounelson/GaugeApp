package com.example.gaugeapp.ui.credit.items

import android.view.View
import com.example.gaugeapp.KolaWhalletApplication.Companion.hiltEntryPoint
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.entities.AirtimeCredit
import com.example.gaugeapp.utils.extentions.convertDateToASpecificFormatWithTodayAndYesterdayInCount
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.textColorResource
import java.util.*

class AirtimeCreditItem(
    var airtimeCredit: AirtimeCredit,
    var airTimeCreditLine: AirTimeCreditLine
) : Item() {
    private val nowDate = Calendar.getInstance().time

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
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

        viewHolder.itemView.id_credit_repay_button.apply {
            visibility = if (airtimeCredit.solved) {
                View.GONE
            } else {
                View.VISIBLE
            }

            setOnClickListener {
                //TODO navigate to transfer page
                val index =
                    airTimeCreditLine.airtimeCreditList.indexOfFirst { it.id == airtimeCredit.id }
                if (index >= 0) {
                    val repository = hiltEntryPoint.airtimeCreditRepository()
                    airTimeCreditLine.airtimeCreditList[index].apply {
                        solved = true
                        repaymentDate = Calendar.getInstance().time
                    }
                    repository.updateCreditLine(airTimeCreditLine)
                }
            }
        }

        viewHolder.itemView.id_credit_solved.visibility = if (airtimeCredit.solved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getTotalAmount(): String {
        val totalAmount = (airtimeCredit.amount * (1 + airTimeCreditLine.payBackPercent))
        return totalAmount.formatNumberWithSpaceBetweenThousand() + " F"
    }

    override fun getLayout() = R.layout.item_credit
}