package com.example.gaugeapp.newBorrowingCampaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gaugeapp.R
import kotlinx.android.synthetic.main.new_borrowing_campaign_fragment.view.*

class NewBorrowingCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = NewBorrowingCampaignFragment()
    }

    private lateinit var viewModel: NewBorrowingCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.new_borrowing_campaign_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_new_borrowing_camp)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}