package com.example.gaugeapp.ui.credit.items

import android.view.View
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.utils.extentions.convertDateToASpecificFormatWithTodayAndYesterdayInCount
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_credit.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.textColorResource
import java.util.*

class ShoppingCreditItem(
    var shoppingCredit: ShoppingCredit,
    var shoppingCreditLine: ShoppingCreditLine
) : Item() {
    private val nowDate = Calendar.getInstance().time

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
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
            text = shoppingCredit.store.name
        }

        viewHolder.itemView.id_credit_real_amount.text =
            shoppingCredit.amount.formatNumberWithSpaceBetweenThousand()


        viewHolder.itemView.id_credit_repay_button.apply {
            visibility = if (shoppingCredit.solved) {
                View.GONE
            } else {
                View.VISIBLE
            }

            setOnClickListener {
                //TODO navigate to transfer page
                val index =
                    shoppingCreditLine.shoppingCreditList.indexOfFirst { it.id == shoppingCredit.id }
                if (index >= 0) {
                    val repository =
                        KolaWhalletApplication.hiltEntryPoint.shoppingCreditRepository()
                    shoppingCreditLine.shoppingCreditList[index].apply {
                        solved = true
                        repaymentDate = Calendar.getInstance().time
                    }
                    repository.updateCreditLine(shoppingCreditLine)
                }
            }
        }

        viewHolder.itemView.id_credit_solved.visibility = if (shoppingCredit.solved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getTotalAmount(): String {
        val totalAmount = (shoppingCredit.amount * (1 + shoppingCreditLine.payBackPercent))
        return totalAmount.formatNumberWithSpaceBetweenThousand() + " F"
    }

    override fun getLayout() = R.layout.item_credit
}