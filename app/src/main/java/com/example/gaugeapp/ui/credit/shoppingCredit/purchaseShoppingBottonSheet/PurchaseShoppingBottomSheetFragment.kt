package com.example.gaugeapp.ui.credit.shoppingCredit.purchaseShoppingBottonSheet

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.ui.credit.shoppingCredit.main.ShoppingCreditMainFragment
import com.example.gaugeapp.utils.extentions.removeSpaces
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_purchase_shopping_botton_sheet.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.toast

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class PurchaseShoppingBottomSheetFragment(
    val store: Store,
    val onComplete: (amount: Double) -> Unit
) :
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
        setOnClickListener()
        updateUI()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        try {
            id_purchase_shopping_store_description.text = "${store.name}, ${store.address}"

            id_amount_shopping_credit.setText(
                ShoppingCreditMainFragment.creditLeft.formatNumberWithSpaceBetweenThousand()
                    .removeSpaces()
            )

            Glide.with(requireContext())
                .load(store.imageUrl)
                .placeholder(R.drawable.ic_image_black)
                .error(R.drawable.ic_image_black)
                .into(id_purchase_shopping_store_image)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnClickListener() {
        id_purchase_shopping_button.setOnClickListener {
            val amount = try {
                id_amount_shopping_credit.text.toString().toDouble()
            } catch (e: Exception) {
                0.0
            }
            if (amount > 0) {
                if (amount <= ShoppingCreditMainFragment.creditLeft) {
                    dismiss()
                    onComplete(amount)
                } else {
                    requireContext().toast(
                        getString(R.string.you_can_only_borrow) + " ${ShoppingCreditMainFragment.creditLeft} FCFA"
                    )
                }
            } else {
                requireContext().toast(R.string.invalid_amount)
            }
        }
    }
}