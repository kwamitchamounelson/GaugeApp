package com.example.gaugeapp.ui.credit.shoppingCredit.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ShoppingCreditLineWithShoppingCreditsList
import com.example.gaugeapp.ui.credit.items.ShoppingCreditHistoryItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_credit_history.*
import kotlinx.android.synthetic.main.layout_credit_history.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.textResource


private const val TAG = "ShoppingCreditHistoryFr"

@AndroidEntryPoint
@InternalCoroutinesApi
class ShoppingCreditHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = ShoppingCreditHistoryFragment()
    }

    private val viewModel by viewModels<ShoppingCreditHistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.shopping_credit_history_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_credit)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
        viewModel.getHistoryOfCredit()
    }


    private fun observeLiveData() {
        viewModel.listShoppingCreditLinetObserver.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                    }
                    is DataState.Success -> {
                        updateUI(dataState.data)
                    }
                    is DataState.Failure -> {
                    }
                }
            })
    }

    private fun updateUI(listShoppingLineWithCredit: List<ShoppingCreditLineWithShoppingCreditsList>?) {
        if (listShoppingLineWithCredit != null) {
            try {

                //toolbar title
                id_credit_toolbar_title.textResource = R.string.shopping_credit_history

                val sumPayBackPercent =
                    listShoppingLineWithCredit.sumByDouble { it.shoppingCreditLine.payBackPercent }

                val totalRealAmount = listShoppingLineWithCredit.sumByDouble { creditLine ->
                    creditLine.creditList.sumByDouble { it.amount }
                }

                val total = (totalRealAmount * (1 + sumPayBackPercent))

                id_credit_history_total_amount.text =
                    total.formatNumberWithSpaceBetweenThousand() + " F"

                val items = arrayListOf<ShoppingCreditHistoryItem>()

                listShoppingLineWithCredit.forEach { creditLine ->
                    creditLine.creditList.forEach { credit ->
                        items.add(ShoppingCreditHistoryItem(credit, creditLine.shoppingCreditLine))
                    }
                }

                val creditItems = items.sortedByDescending { it.shoppingCredit.repaymentDate }
                id_credit_history_list_rv.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = GroupAdapter<ViewHolder>().apply {
                        add(Section(creditItems))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                try {
                    findNavController().navigateUp()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}