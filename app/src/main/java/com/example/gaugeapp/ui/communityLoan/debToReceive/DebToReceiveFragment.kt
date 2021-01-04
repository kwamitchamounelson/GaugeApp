package com.example.gaugeapp.ui.communityLoan.debToReceive

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

class DebToReceiveFragment : Fragment() {

    companion object {
        fun newInstance() = DebToReceiveFragment()
    }

    private lateinit var viewModel: DebToReceiveViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.deb_to_receive_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DebToReceiveViewModel::class.java)
        // TODO: Use the ViewModel
    }

}