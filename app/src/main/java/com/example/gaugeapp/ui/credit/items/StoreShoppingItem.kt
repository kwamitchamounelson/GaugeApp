package com.example.gaugeapp.ui.credit.items

import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.Store
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_store_shopping.view.*

class StoreShoppingItem(var store: Store) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_shopping_store_name.text = store.name
        viewHolder.itemView.id_shopping_store_location.text = store.address

        Glide.with(viewHolder.itemView)
            .load(store.imageUrl)
            .placeholder(R.drawable.ic_image_black)
            .error(R.drawable.ic_image_black)
            .into(viewHolder.itemView.id_shopping_store_image)
    }

    override fun getLayout() = R.layout.item_store_shopping
}