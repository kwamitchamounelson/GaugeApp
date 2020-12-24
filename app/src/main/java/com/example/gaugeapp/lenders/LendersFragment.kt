package com.example.gaugeapp.lenders

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.items.LenderItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.lenders_fragment.*
import kotlinx.android.synthetic.main.lenders_fragment.view.*

class LendersFragment : Fragment() {

    companion object {
        fun newInstance() = LendersFragment()
    }

    private lateinit var viewModel: LendersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.lenders_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_lender)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        //TODO to remove
        val items = (0..20).map { LenderItem() }
        id_lender_list_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_guarantors, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}