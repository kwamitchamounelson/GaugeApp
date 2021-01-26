package com.example.gaugeapp.ui.credit.shoppingCredit.parteneredStores

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
import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.ui.credit.items.StoreShoppingItem
import com.example.gaugeapp.ui.credit.shoppingCredit.main.ShoppingCreditMainFragment
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.ui.credit.shoppingCredit.purchaseShoppingBottonSheet.PurchaseShoppingBottomSheetFragment
import com.example.gaugeapp.utils.printLogD
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.partenered_stores_fragment.*
import kotlinx.android.synthetic.main.partenered_stores_fragment.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.toast
import java.util.*

private const val TAG = "PartnerStoresFragmen"
@ExperimentalCoroutinesApi
@AndroidEntryPoint
@InternalCoroutinesApi
class PartnerStoresFragment : Fragment() {

    companion object {
        fun newInstance() = PartnerStoresFragment()
    }

    private val viewModel by viewModels<ParteneredStoresViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.partenered_stores_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_store_shopping)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
        viewModel.getAllStores()
    }

    private fun observeLiveData() {
        viewModel.storeListObserver.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    updateUi(dataState.data)
                }
                is DataState.Failure -> {
                }
            }
        })
    }

    private fun updateUi(storesList: List<Store>?) {
        if (storesList != null) {
            val items = storesList.map {
                StoreShoppingItem(it)
            }
            updateRV(items)
        }
    }

    private fun updateRV(items: List<StoreShoppingItem>) {
        try {
            id_store_shopping_rv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = GroupAdapter<ViewHolder>().apply {
                    add(Section(items))

                    setOnItemClickListener { item, _ ->
                        val store = (item as StoreShoppingItem).store
                        val bs = PurchaseShoppingBottomSheetFragment(store) { amount ->
                            val nowDate = Calendar.getInstance().time

                            val shoppingCreditRequest = ShoppingCreditRequest(
                                "",
                                ShoppingCreditMainFragment.currentShoppingCreditLine!!.id,
                                amount,
                                store,
                                viewModel.userId,
                                nowDate,
                                ENUM_REQUEST_STATUS.PENDING,
                                nowDate,
                                true
                            )

                            printLogD(TAG,"new shoppingCreditRequest to create=> $shoppingCreditRequest")

                            viewModel.requestBorrowShopping(
                                shoppingCreditRequest,
                                onSuccess = {
                                    try {
                                        findNavController().navigateUp()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                },
                                onError = {
                                    requireContext().toast(R.string.connection_error_please_try_again)
                                }
                            )
                        }
                        bs.show(childFragmentManager, "")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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