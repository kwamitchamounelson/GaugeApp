package com.example.gaugeapp.ui.communityLoan.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_profile.view.*

class ImageProfileItem(val imageUrl: String) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView)
            .load(imageUrl)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_image_url)
    }

    override fun getLayout() = R.layout.item_image_profile
}