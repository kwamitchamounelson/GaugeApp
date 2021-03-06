package com.example.gaugeapp.ui.communityLoan.items

import android.graphics.Color
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_campaign.view.*

class CampaignItem(val color: Int? = null) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView)
            .load("https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb")
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_campaign_item_user_image)

        if (color != null) {
            viewHolder.itemView.id_campaign_item_parent.setCardBackgroundColor(color)
        } else {
            val someColor =
                listOf(Color.parseColor("#FFF6E6"), Color.parseColor("#E2D3E1")).random()
            viewHolder.itemView.id_campaign_item_parent.setCardBackgroundColor(someColor)
        }
    }

    override fun getLayout() = R.layout.item_campaign
}