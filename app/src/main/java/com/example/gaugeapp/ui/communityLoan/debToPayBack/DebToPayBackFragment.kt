package com.example.gaugeapp.ui.communityLoan.debToPayBack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.DebToPayBackItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.deb_to_pay_back_fragment.*
import kotlinx.android.synthetic.main.deb_to_pay_back_fragment.view.*

class DebToPayBackFragment : Fragment() {

    companion object {
        fun newInstance() = DebToPayBackFragment()
    }

    private lateinit var viewModel: DebToPayBackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.deb_to_pay_back_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_deb_to_pay_back)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DebToPayBackViewModel::class.java)
        updateUi()
    }

    private fun updateUi() {
        val items = (0..10).map {
            DebToPayBackItem()
        }

        id_list_deb_to_pay_back_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}