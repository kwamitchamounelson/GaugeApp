package com.example.gaugeapp.ui.exchange.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_nearby_agency.view.*

class NearbyAgencyItem() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val bankImage =
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/western_union.png?alt=media&token=c64663dc-13bf-4e1d-940b-64722ccc6636"

        Glide.with(viewHolder.itemView)
            .load(bankImage)
            .placeholder(R.drawable.ic_image_black)
            .error(R.drawable.ic_image_black)
            .centerCrop()
            .into(viewHolder.itemView.id_nearby_agencie_image)
    }

    override fun getLayout() = R.layout.item_nearby_agency
}