package com.example.gaugeapp.ui.communityLoan.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.GuarantorComLoan
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_guarantor.view.*

class GuarantorItem(val guarantor: GuarantorComLoan) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_guarantor_name.text = guarantor.userFullName
        viewHolder.itemView.id_guarantor_score.text = "${guarantor.score.toInt().toString()} pts"

        Glide.with(viewHolder.itemView)
            .load(guarantor.imageUrl)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_guarantor_image)
    }

    override fun getLayout() = R.layout.item_guarantor
}