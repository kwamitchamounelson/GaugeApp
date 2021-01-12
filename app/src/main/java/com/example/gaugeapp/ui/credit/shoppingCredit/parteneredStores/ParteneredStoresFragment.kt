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
import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.ui.credit.items.StoreShoppingItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.ui.credit.shoppingCredit.purchaseShoppingBottonSheet.PurchaseShoppingBottonSheetFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.partenered_stores_fragment.*
import kotlinx.android.synthetic.main.partenered_stores_fragment.view.*
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
@InternalCoroutinesApi
class ParteneredStoresFragment : Fragment() {

    companion object {
        fun newInstance() = ParteneredStoresFragment()
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
                        val bs = PurchaseShoppingBottonSheetFragment(store) {
                            //TODO implement purchase
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