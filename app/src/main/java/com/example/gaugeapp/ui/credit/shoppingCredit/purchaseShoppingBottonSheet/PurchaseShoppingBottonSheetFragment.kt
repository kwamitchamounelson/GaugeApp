package com.example.gaugeapp.ui.credit.shoppingCredit.purchaseShoppingBottonSheet

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.gaugeapp.entities.Store
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_purchase_shopping_botton_sheet.*

@AndroidEntryPoint
class PurchaseShoppingBottonSheetFragment(val store: Store, val onComplete: () -> Unit) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_shopping_botton_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListner()
        updateUI()
    }

    private fun updateUI() {
        try {
            id_purchase_shopping_store_description.text = store.description

            Glide.with(requireContext())
                .load(store.imageUrl)
                .placeholder(R.drawable.ic_image_black)
                .error(R.drawable.ic_image_black)
                .into(id_purchase_shopping_store_image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnClickListner() {
        id_purchase_shopping_close.setOnClickListener {
            dismiss()
        }

        id_purchase_shopping_button.setOnClickListener {
            dismiss()
            onComplete()
        }
    }
}