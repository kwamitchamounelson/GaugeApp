package com.example.gaugeapp.interestCampaignBottomSheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.items.InterestRateItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.interest_campaign_bottom_sheet_fragment.*

class InterestCampaignBottomSheetFragment(
    private val amount: Int = 1000,
    private val currentInterest: Int = 5,
    val onComplete: (interest: Int) -> Unit
) :
    BottomSheetDialogFragment() {

    //we load items form 0.05 to 0.3 (5% to 30%)
    private var interestItems = (5..30).map { InterestRateItem(it) }
    private var selectedInterest: Int = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.interest_campaign_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListner()
        updateUI()
    }

    private fun updateUI() {
        //init rv of amount
        id_borrow_cam_interest_rate_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(interestItems))
                setOnItemClickListener { item, _ ->
                    updateSlider((item as InterestRateItem).interest)
                }
            }
        }

        id_borrow_cam_interest_rate_selected.text = "$currentInterest%"

        id_amount_interest.text = "${(amount * (currentInterest / 100.0)).toInt()} F"

        //init slider
        id_interest_rate_progression.apply {
            valueFrom = 5F
            stepSize = 1F
            valueTo = 30F
            value = currentInterest.toFloat()
        }
    }

    private fun updateSlider(interest: Int) {
        id_interest_rate_progression.value = interest.toFloat()
    }

    private fun setOnClickListner() {
        //listen slide
        id_interest_rate_progression.addOnChangeListener { _, value, _ ->
            selectedInterest = value.toInt()

            id_borrow_cam_interest_rate_selected.text = "$selectedInterest%"
            id_amount_interest.text = "${(amount * (selectedInterest / 100.0)).toInt()} F"

            interestItems.forEach {
                it.apply {
                    this.itemSelected = selectedInterest == this.interest
                    this.notifyChanged()
                }
            }

            //we scroll to selected amount
            val selectedIndex = interestItems.indexOfFirst { it.itemSelected }
            id_borrow_cam_interest_rate_rv.scrollToPosition(selectedIndex)
        }

        id_interest_rate_button.setOnClickListener {
            dismiss()
            onComplete(selectedInterest)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onComplete(selectedInterest)
    }

}