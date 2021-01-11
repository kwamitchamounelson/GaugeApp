package com.example.gaugeapp.ui.exchange.reviewAgencyBottomSheet

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
import kotlinx.android.synthetic.main.review_agency_bottom_sheet_fragment.*

class ReviewAgencyBottomSheetFragment() : BottomSheetDialogFragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.review_agency_bottom_sheet_fragment, container, false)
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
        val bankImage =
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/western_union.png?alt=media&token=c64663dc-13bf-4e1d-940b-64722ccc6636"
        Glide.with(this)
            .load(bankImage)
            .placeholder(R.drawable.ic_image_black)
            .error(R.drawable.ic_image_black)
            .centerCrop()
            .into(id_review_agency_image)
    }

    private fun setOnClickListner() {
        id_submit_review_agency_button.setOnClickListener {
            dismiss()
        }
        id_review_agency_cancel.setOnClickListener {
            dismiss()
        }
    }

}