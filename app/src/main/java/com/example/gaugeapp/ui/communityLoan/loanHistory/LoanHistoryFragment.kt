package com.example.gaugeapp.ui.communityLoan.loanHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.items.LoanHistoryItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.loan_history_fragment.*
import kotlinx.android.synthetic.main.loan_history_fragment.view.*

class LoanHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = LoanHistoryFragment()
    }

    private lateinit var viewModel: LoanHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.loan_history_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_loan_history)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUi()
    }

    private fun updateUi() {
        val items = (0..10).map {
            LoanHistoryItem()
        }

        id_list_loan_history_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

}