package com.example.gaugeapp.ui.communityLoan.addContactBottomSheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.GuarantorComLoan
import com.example.gaugeapp.ui.communityLoan.items.ContactGuarantorItem
import com.example.gaugeapp.ui.communityLoan.items.ImageProfileSelectedItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.add_contact_bottom_sheet_fragment.*
import kotlinx.android.synthetic.main.add_contact_bottom_sheet_fragment.view.*

class AddContactBottomSheetFragment(
    private val guarantorList: ArrayList<GuarantorComLoan> = arrayListOf(),
    val onComplete: (list: ArrayList<GuarantorComLoan>) -> Unit
) :
    BottomSheetDialogFragment() {

    private var selectedGuarantors = arrayListOf<ImageProfileSelectedItem>().apply {
        addAll(guarantorList.map { ImageProfileSelectedItem(it) })
    }

    private var allContactGuarantorItem = arrayListOf<ContactGuarantorItem>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_contact_bottom_sheet_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_add_contact)
        setHasOptionsMenu(true)
        return root
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

        laodAllContact()

        loadSelectedContact()
    }

    private fun laodAllContact() {

        //TODO to remove
        (670000000..670000020).forEach { phoneNumberGua ->
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
                phoneNumber = phoneNumberGua.toString()
            }

            val item = ContactGuarantorItem(guarantor).apply {
                itemIsChecked =
                    selectedGuarantors.contains(ImageProfileSelectedItem(this.guarantor))
            }

            allContactGuarantorItem.add(item)
        }

        id_confirm__all_contact_bs_button.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(allContactGuarantorItem))
                setOnItemClickListener { item, view ->
                    item as ContactGuarantorItem
                    if (item.itemIsChecked) {
                        selectedGuarantors.remove(ImageProfileSelectedItem(item.guarantor))
                    } else {
                        selectedGuarantors.add(ImageProfileSelectedItem(item.guarantor))
                    }
                    item.apply {
                        this.itemIsChecked = !this.itemIsChecked
                        notifyChanged()
                    }
                    loadSelectedContact()
                }
            }
        }

    }

    private fun loadSelectedContact() {
        id_confirm__selected_contact_bs_button.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(selectedGuarantors))
            }
        }
    }

    private fun setOnClickListner() {
        id_confirm_contact_bs_button.setOnClickListener {
            dismiss()
            onComplete(selectedGuarantors.map { it.guarantor } as ArrayList<GuarantorComLoan>)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onComplete(selectedGuarantors.map { it.guarantor } as ArrayList<GuarantorComLoan>)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                dismiss()
                onComplete(selectedGuarantors.map { it.guarantor } as ArrayList<GuarantorComLoan>)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}