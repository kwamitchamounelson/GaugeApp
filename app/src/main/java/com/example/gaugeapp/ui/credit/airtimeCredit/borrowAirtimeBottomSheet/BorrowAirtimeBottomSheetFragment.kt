package com.example.gaugeapp.ui.credit.airtimeCredit.borrowAirtimeBottomSheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.R
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.ui.credit.ConstantCredit.INTEREST_RATE
import com.example.gaugeapp.utils.PhoneNumberUtils
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.example.gaugeapp.utils.getOperatorFromList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.gaugeapp.ui.credit.items.AmountBorowCreditItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.borrow_airtime_bottom_sheet_fragment.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.toast


@AndroidEntryPoint
class BorrowAirtimeBottomSheetFragment(val onComplete: (amount: Int, phoneNumber: String) -> Unit) :
    BottomSheetDialogFragment() {

    private var selectedAmount: Int? = null

    private var selectedPhoneNumber: String? = null

    private var mtnPhone = ""
    private var orangePhon = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.borrow_airtime_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListner()
        updateUI()
    }

    private fun setOnClickListner() {
        id_amount_credit_borrow_button.setOnClickListener {
            try {
                when {
                    selectedAmount == null -> {
                        requireContext().toast(R.string.please_select_an_amount)
                    }
                    selectedPhoneNumber == null -> {
                        requireContext().toast(R.string.please_select_an_phone_number)
                    }
                    else -> {
                        dismiss()
                        onComplete(selectedAmount!!, selectedPhoneNumber!!)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        id_user_mtn_number.setOnClickListener {
            id_user_mtn_number.textColorResource = R.color.white
            id_user_mtn_number.backgroundResource = R.drawable.background_my_number_mtn
            if (mtnPhone.isNotEmpty()) {
                selectedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(mtnPhone)
                id_user_orange_number.apply {
                    textColorResource = R.color.colorPrimary
                    backgroundResource = R.drawable.background_my_number_orange_desable
                }
            }
        }


        id_user_orange_number.setOnClickListener {
            id_user_orange_number.textColorResource = R.color.white
            id_user_orange_number.backgroundResource = R.drawable.background_my_number_orange
            if (orangePhon.isNotEmpty()) {
                selectedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(orangePhon)
                id_user_mtn_number.apply {
                    textColorResource = R.color.colorPrimary
                    backgroundResource = R.drawable.background_my_number_mtn_desable
                }
            }
        }
    }

    private fun updateUI() {
        id_credit_bottom_sheet_amount.text = ""
        id_credit_bottom_sheet_fees.text = ""
        id_credit_bottom_sheet_total.text = ""

        //we load items form 100 to 500
        val amountItems = (100..500 step 100).map {
            AmountBorowCreditItem(it)
        }

        id_credit_bottom_sheet_amount_list_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(amountItems))
                setOnItemClickListener { item, _ ->
                    selectedAmount = (item as AmountBorowCreditItem).amount
                    showDedails()
                    amountItems.forEach {
                        it.apply {
                            this.itemSelected = item.amount == this.amount
                            this.notifyChanged()
                        }
                    }
                }
            }
        }

        //phone number
        id_user_mtn_number.apply {
            getOperatorFromList(
                KolaWhalletApplication.currentUser.mobileMoneyNumbers,
                ENUMOPERATEUR.MTN_MONEY.name
            )?.let {
                mtnPhone = it.phoneNumber
            }
            if (mtnPhone.isNotEmpty()) {
                visibility = View.VISIBLE
                text = PhoneNumberUtils.remove237ToPhoneNumber(mtnPhone)
            } else {
                visibility = View.GONE
            }
        }

        id_user_orange_number.apply {
            getOperatorFromList(
                KolaWhalletApplication.currentUser.mobileMoneyNumbers,
                ENUMOPERATEUR.ORANGE_MONEY.name
            )?.let {
                orangePhon = it.phoneNumber
            }
            if (orangePhon.isNotEmpty()) {
                visibility = View.VISIBLE
                text = PhoneNumberUtils.remove237ToPhoneNumber(orangePhon)
            } else {
                visibility = View.GONE
            }
        }
    }

    private fun showDedails() {
        selectedAmount?.let {
            id_credit_bottom_sheet_amount.text = it.toString()

            id_credit_bottom_sheet_fees.text =
                (it * INTEREST_RATE).formatNumberWithSpaceBetweenThousand()

            id_credit_bottom_sheet_total.text =
                (it * (1 + INTEREST_RATE)).formatNumberWithSpaceBetweenThousand()
        }
    }
}