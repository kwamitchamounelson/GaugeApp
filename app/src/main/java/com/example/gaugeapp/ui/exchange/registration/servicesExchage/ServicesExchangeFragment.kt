package com.example.gaugeapp.ui.exchange.registration.servicesExchage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

class ServicesExchangeFragment : Fragment() {

    companion object {
        fun newInstance() = ServicesExchangeFragment()
    }

    private lateinit var viewModel: ServicesExchangeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.services_exchange_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ServicesExchangeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}