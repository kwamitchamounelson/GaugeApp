package com.example.gaugeapp.ui.savingGroup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

class SavingGroupMainFragment : Fragment() {

    companion object {
        fun newInstance() = SavingGroupMainFragment()
    }

    private lateinit var viewModel: SavingGroupMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saving_group_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SavingGroupMainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}