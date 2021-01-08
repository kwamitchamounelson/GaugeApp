package com.example.gaugeapp.ui.exchange.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_rating.view.*

class RatingItem() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val userImage =
            "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"

        Glide.with(viewHolder.itemView)
            .load(userImage)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_ratings_user_image)
    }

    override fun getLayout() = R.layout.item_rating
}