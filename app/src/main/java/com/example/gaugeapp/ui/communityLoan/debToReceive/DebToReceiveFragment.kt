package com.example.gaugeapp.ui.communityLoan.debToReceive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.DebToReceiveItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.deb_to_receive_fragment.*
import kotlinx.android.synthetic.main.deb_to_receive_fragment.view.*

class DebToReceiveFragment : Fragment() {

    companion object {
        fun newInstance() = DebToReceiveFragment()
    }

    private lateinit var viewModel: DebToReceiveViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.deb_to_receive_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_deb_to_receive)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi()
    }

    private fun updateUi() {
        val items = (0..10).map {
            DebToReceiveItem()
        }

        id_list_deb_to_receive_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}