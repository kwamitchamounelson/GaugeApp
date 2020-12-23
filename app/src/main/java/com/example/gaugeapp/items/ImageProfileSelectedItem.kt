package com.example.gaugeapp.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.GuarantorComLoan
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_contact_selected_campaign.view.*

class ImageProfileSelectedItem(val guarantor: GuarantorComLoan) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView)
            .load(guarantor.imageUrl)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_contact_image)
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return (other is ImageProfileSelectedItem) && (other.guarantor.phoneNumber == guarantor.phoneNumber)
    }

    override fun equals(other: Any?): Boolean {
        return (other is ImageProfileSelectedItem) && (other.guarantor.phoneNumber == guarantor.phoneNumber)
    }

    override fun getLayout() = R.layout.item_contact_selected_campaign
}