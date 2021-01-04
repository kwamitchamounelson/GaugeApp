package com.example.gaugeapp.ui.communityLoan.items

import android.view.View
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.GuarantorComLoan
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_contact_guarantor.view.*

class ContactGuarantorItem(val guarantor: GuarantorComLoan) : Item() {

    var itemIsChecked = false

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_add_contact_name.text = guarantor.userFullName
        viewHolder.itemView.id_add_contact_phone_number.text = guarantor.phoneNumber

        Glide.with(viewHolder.itemView)
            .load(guarantor.imageUrl)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_add_contact_image)

        if (itemIsChecked) {
            viewHolder.itemView.id_add_contact_checked.visibility = View.VISIBLE
            viewHolder.itemView.id_add_contact_unchecked.visibility = View.GONE
        } else {
            viewHolder.itemView.id_add_contact_checked.visibility = View.GONE
            viewHolder.itemView.id_add_contact_unchecked.visibility = View.VISIBLE
        }

    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return (other is ContactGuarantorItem) && (other.guarantor.phoneNumber == guarantor.phoneNumber)
    }

    override fun equals(other: Any?): Boolean {
        return (other is ContactGuarantorItem) && (other.guarantor.phoneNumber == guarantor.phoneNumber)
    }

    override fun getLayout() = R.layout.item_contact_guarantor
}