package com.example.gaugeapp.guaranteeing

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.items.CampaignItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.guaranteeing_fragment.*
import kotlinx.android.synthetic.main.guaranteeing_fragment.view.*

class GuaranteeingFragment : Fragment() {

    companion object {
        fun newInstance() = GuaranteeingFragment()
    }

    private lateinit var viewModel: GuaranteeingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.guaranteeing_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_guaranteeing)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi()
    }

    private fun updateUi() {
        val items = (0..10).map {
            CampaignItem(Color.parseColor("#FFF6E6"))
        }

        id_guaranteeing_list_main_rv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}