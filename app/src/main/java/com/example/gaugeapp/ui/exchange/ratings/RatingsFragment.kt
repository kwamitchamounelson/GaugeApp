package com.example.gaugeapp.ui.exchange.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.exchange.items.RatingItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.ratings_fragment.*

class RatingsFragment : Fragment() {

    companion object {
        fun newInstance() = RatingsFragment()
    }

    private lateinit var viewModel: RatingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ratings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RatingsViewModel::class.java)
        updateUI()
    }

    private fun updateUI() {
        val bankImage =
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/western_union.png?alt=media&token=c64663dc-13bf-4e1d-940b-64722ccc6636"
        Glide.with(this)
            .load(bankImage)
            .placeholder(R.drawable.ic_image_black)
            .error(R.drawable.ic_image_black)
            .centerCrop()
            .into(id_ratings_bank_image)


        val items = (0..10).map {
            RatingItem()
        }

        id_ratings_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }


}