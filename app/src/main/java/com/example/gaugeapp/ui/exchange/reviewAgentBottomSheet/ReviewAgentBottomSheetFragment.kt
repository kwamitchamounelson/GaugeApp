package com.example.gaugeapp.ui.exchange.reviewAgentBottomSheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.review_agent_bottom_sheet_fragment.*

class ReviewAgentBottomSheetFragment() : BottomSheetDialogFragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.review_agent_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        try {
            bottomSheetBehavior = (dialog as BottomSheetDialog).behavior
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setOnClickListner()
        updateUI()
    }

    private fun updateUI() {
        val userImage =
            "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"
        Glide.with(this)
            .load(userImage)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_submit_review_agent_image)
    }

    private fun setOnClickListner() {
        id_submit_review_agent_button.setOnClickListener {
            dismiss()
        }
        id_review_agent_cancel.setOnClickListener {
            dismiss()
        }
    }

}