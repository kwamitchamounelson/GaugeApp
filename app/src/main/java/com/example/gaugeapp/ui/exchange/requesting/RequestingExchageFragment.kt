package com.example.gaugeapp.ui.exchange.requesting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.exchange.reviewAgencyBottomSheet.ReviewAgencyBottomSheetFragment
import com.example.gaugeapp.ui.exchange.reviewAgentBottomSheet.ReviewAgentBottomSheetFragment
import kotlinx.android.synthetic.main.requesting_exchange_fragment.*

class RequestingExchageFragment : Fragment() {

    companion object {
        fun newInstance() = RequestingExchageFragment()
    }

    private lateinit var viewModel: RequestingExchageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.requesting_exchange_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnclick()
    }

    private fun setOnclick() {
        id_ratings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_exchange_to_ratingsFragment)
        }

        id_agency.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_exchange_to_agenceFragment)
        }

        id_review_agency.setOnClickListener {
            val bs = ReviewAgencyBottomSheetFragment()
            bs.show(childFragmentManager, "")
        }

        id_review_agent.setOnClickListener {
            val bs = ReviewAgentBottomSheetFragment()
            bs.show(childFragmentManager, "")
        }
    }

}