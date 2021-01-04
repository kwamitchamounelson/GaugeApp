package com.example.gaugeapp.ui.exchange.requesting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

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
        viewModel = ViewModelProvider(this).get(RequestingExchageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}