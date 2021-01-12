package com.example.gaugeapp.ui.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gaugeapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.credit_main_fragment.*
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
@InternalCoroutinesApi
class CreditMainFragment : Fragment() {

    companion object {
        fun newInstance() = CreditMainFragment()
    }

    private val viewModel by viewModels<CreditMainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.credit_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnclick()
    }


    private fun setOnclick() {
        id_airtime_credit.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_credit_to_airtimeCreditMainFragment)
        }

        id_shopping_credit.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_credit_to_shoppingCreditMainFragment)
        }
    }

}