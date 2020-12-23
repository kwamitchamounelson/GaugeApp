package com.example.gaugeapp.addGuarantorBottomSheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.addContactBottomSheet.AddContactBottomSheetFragment
import com.example.gaugeapp.entities.GuarantorComLoan
import com.example.gaugeapp.items.AddGuarantorItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.add_guarantor_bottom_sheet_fragment.*

class AddGuarantorBottomSheetFragment(
    private val guarantorList: ArrayList<GuarantorComLoan> = arrayListOf(),
    val onComplete: (list: ArrayList<GuarantorComLoan>) -> Unit
) :
    BottomSheetDialogFragment() {

    private var selectedGuarantors = guarantorList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_guarantor_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnClickListner()
        updateUI()
    }

    private fun updateUI() {

        val items = selectedGuarantors.map {
            AddGuarantorItem(it)
        }

        id_add_guarantor_bs_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }

        if (items.isEmpty()) {
            id_add_guarantor_bs_empty_list.visibility = View.VISIBLE
            id_add_guarantor_bs_rv.visibility = View.GONE
        } else {
            id_add_guarantor_bs_empty_list.visibility = View.GONE
            id_add_guarantor_bs_rv.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListner() {
        id_add_guarantor_bs_empty_list.setOnClickListener {
            addGuarantor()
        }

        id_add_guarantor_bs_button.setOnClickListener {
            addGuarantor()
        }
    }

    private fun addGuarantor() {
        val bs = AddContactBottomSheetFragment(selectedGuarantors) { list ->
            selectedGuarantors = list
            updateUI()
        }
        bs.show(childFragmentManager, "")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onComplete(selectedGuarantors)
    }

}