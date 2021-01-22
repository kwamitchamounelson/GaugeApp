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
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.ui.credit.items.ShoppingCreditHistoryItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.example.gaugeapp.utils.printLogD
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_credit_history.*
import kotlinx.android.synthetic.main.layout_credit_history.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.textResource
import org.jetbrains.anko.toast


private const val TAG = "ShoppingCreditHistoryFr"

@AndroidEntryPoint
@InternalCoroutinesApi
class ShoppingCreditHistoryFragment : Fragment() {

    companion object {
        fun newInstance() = ShoppingCreditHistoryFragment()
    }

    private val viewModel by viewModels<ShoppingCreditHistoryViewModel>()

    private var firstTime = true

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
        try {
            //toolbar title
            id_credit_toolbar_title.textResource = R.string.shopping_credit_history

            id_credit_history_total_amount.text = ""

        } catch (e: Exception) {
            e.printStackTrace()
        }
        observeLiveData()
        viewModel.setStateEvent(ShoppingCreditHistoryStateEven.GetDataFirstTime)
    }

    private fun observeLiveData() {
        viewModel.listShoppingCreditLinetFirstTimeObserver.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                    }
                    is DataState.Success -> {
                        val creditLineList = dataState.data ?: listOf()
                        printLogD(TAG, "creditLineList $creditLineList")
                        updateUI(creditLineList)
                        if (firstTime) {
                            firstTime = false
                            if (creditLineList.isNotEmpty()) {
                                val lastCreditLine = creditLineList.last()
                                viewModel.setStateEvent(
                                    ShoppingCreditHistoryStateEven.GetDataAfterFirstTime(
                                        lastCreditLine
                                    )
                                )
                            }
                        }
                    }
                    is DataState.Failure -> {
                        requireContext().toast(R.string.connection_error_please_try_again)
                        printLogD(TAG, " ${dataState.throwable}")
                    }
                }
            })
    }

    private fun updateUI(shoppingCreditLineList: List<ShoppingCreditLine>?) {
        if (shoppingCreditLineList != null) {
            try {
                val total = shoppingCreditLineList.sumByDouble { creditLine ->
                    val amount = (creditLine.shoppingCreditList.sumByDouble { it.amount })
                    val percent = creditLine.payBackPercent
                    (amount * percent + amount)
                }
                printLogD(TAG, "total $total")

                id_credit_history_total_amount.text =
                    total.formatNumberWithSpaceBetweenThousand() + " F"

                val items = arrayListOf<ShoppingCreditHistoryItem>()

                shoppingCreditLineList.forEach { creditLine ->
                    creditLine.shoppingCreditList.forEach { credit ->
                        items.add(ShoppingCreditHistoryItem(credit, creditLine))
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