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

        val items = guarantorList.map {
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
        }else{
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
        //TODO to remove
        val image = listOf(
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb",
            "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"
        ).random()

        val scoreRandom = (0..100).random().toDouble()

        val name = listOf("Arthuro Oss", "Sashila Hax", "Boris Bean").random()

        val guarantor = GuarantorComLoan().apply {
            imageUrl = image
            score = scoreRandom
            userFullName = name
        }
        guarantorList.add(guarantor)
        updateUI()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onComplete(guarantorList)
    }

}