package com.example.gaugeapp.ui.communityLoan.amountCampaignBottomSheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.AmountBorowCreditItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.amount_campaign_bottom_sheet_fragment.*

class AmountCampaignBottomSheetFragment(
    private val currentAmount: Int = 1000,
    val onComplete: (amount: Int) -> Unit
) :
    BottomSheetDialogFragment() {

    //we load items form 1000 to 15 000
    private var amountItems = (1000..15000 step 1000).map { AmountBorowCreditItem(it) }
    private var selectedAmount: Int = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.amount_campaign_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListner()
        updateUI()
    }

    private fun updateUI() {
        //init rv of amount
        id_borrow_cam_amount_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(amountItems))
                setOnItemClickListener { item, _ ->
                    updateSlider((item as AmountBorowCreditItem).amount)
                }
            }
        }

        id_borrow_cam_amount_selected.text = "$currentAmount F"

        //init slider
        id_borrow_cam_progression.apply {
            valueFrom = 1000F
            stepSize = 1000F
            valueTo = 15000F
            value = currentAmount.toFloat()
        }
    }

    private fun updateSlider(amount: Int) {
        id_borrow_cam_progression.value = amount.toFloat()
    }

    private fun setOnClickListner() {
        //listen slide
        id_borrow_cam_progression.addOnChangeListener { _, value, _ ->
            selectedAmount = value.toInt()

            id_borrow_cam_amount_selected.text = "$selectedAmount F"

            amountItems.forEach {
                it.apply {
                    this.itemSelected = selectedAmount == this.amount
                    this.notifyChanged()
                }
            }

            //we scroll to selected amount
            val selectedIndex = amountItems.indexOfFirst { it.itemSelected }
            id_borrow_cam_amount_rv.scrollToPosition(selectedIndex)
        }

        id_amount_credit_borrow_button.setOnClickListener {
            dismiss()
            onComplete(selectedAmount)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onComplete(selectedAmount)
    }

}