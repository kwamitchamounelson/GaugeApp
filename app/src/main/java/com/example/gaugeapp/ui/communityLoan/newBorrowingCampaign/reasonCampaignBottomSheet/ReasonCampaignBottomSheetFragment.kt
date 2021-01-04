package com.example.gaugeapp.ui.communityLoan.newBorrowingCampaign.reasonCampaignBottomSheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ENUM_BORROWING_REASON
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.reason_campaign_bottom_sheet_fragment.*

class ReasonCampaignBottomSheetFragment(
    private val currentBorrowingReason: ENUM_BORROWING_REASON? = null,
    val onComplete: (borrowingReason: ENUM_BORROWING_REASON?) -> Unit
) :
    BottomSheetDialogFragment() {

    private var selectedBorrowingReason: ENUM_BORROWING_REASON? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reason_campaign_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListner()
        updateUI(currentBorrowingReason)
    }

    private fun updateUI(borrowingReason: ENUM_BORROWING_REASON?) {
        selectedBorrowingReason = borrowingReason
        id_borrowing_reason_block1.children.forEach {
            if (it is TextView) {
                it.isEnabled = true
            }
        }

        id_borrowing_reason_block2.children.forEach {
            if (it is TextView) {
                it.isEnabled = true
            }
        }
        if (borrowingReason != null) {
            when (borrowingReason) {
                ENUM_BORROWING_REASON.HEALTH -> {
                    id_borrowing_reason_health.isEnabled = false
                }
                ENUM_BORROWING_REASON.FOOD -> {
                    id_borrowing_reason_food.isEnabled = false
                }
                ENUM_BORROWING_REASON.SCHOOL -> {
                    id_borrowing_reason_school.isEnabled = false
                }
                ENUM_BORROWING_REASON.TRANSPORT -> {
                    id_borrowing_reason_transport.isEnabled = false
                }
                ENUM_BORROWING_REASON.EMERGENCY -> {
                    id_borrowing_reason_emergency.isEnabled = false
                }
            }
        }
    }

    private fun setOnClickListner() {
        id_borrowing_reason_button.setOnClickListener {
            dismiss()
            onComplete(selectedBorrowingReason)
        }

        id_borrowing_reason_health.setOnClickListener {
            updateUI(ENUM_BORROWING_REASON.HEALTH)
        }

        id_borrowing_reason_food.setOnClickListener {
            updateUI(ENUM_BORROWING_REASON.FOOD)
        }

        id_borrowing_reason_school.setOnClickListener {
            updateUI(ENUM_BORROWING_REASON.SCHOOL)
        }

        id_borrowing_reason_transport.setOnClickListener {
            updateUI(ENUM_BORROWING_REASON.TRANSPORT)
        }

        id_borrowing_reason_emergency.setOnClickListener {
            updateUI(ENUM_BORROWING_REASON.EMERGENCY)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onComplete(selectedBorrowingReason)
    }

}