package com.example.gaugeapp.ui.communityLoan.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_loan_history.view.*

class LoanHistoryItem() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        val image1 =
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb"

        val image2 =
            "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"

        val image = listOf(image1, image2).random()

        Glide.with(viewHolder.itemView)
            .load(image)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(viewHolder.itemView.id_loan_history_user_image)
    }

    override fun getLayout() = R.layout.item_loan_history
}