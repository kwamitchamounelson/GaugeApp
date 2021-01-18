package com.example.gaugeapp.ui.credit.shoppingCredit.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ShoppingCreditLineWithShoppingCreditsList
import com.example.gaugeapp.ui.credit.items.ShoppingCreditItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.example.gaugeapp.utils.permissionsutils.FragmentPermissions
import com.example.gaugeapp.utils.permissionsutils.askAnyPermission
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_credit_main.*
import kotlinx.android.synthetic.main.layout_credit_main.view.*
import kotlinx.android.synthetic.main.layout_map_shopping_credit_bottom_cheet.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textResource
import java.util.*

@AndroidEntryPoint
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class ShoppingCreditMainFragment : FragmentPermissions(), OnMapReadyCallback {

    companion object {
        fun newInstance() =
            ShoppingCreditMainFragment()

        private const val MAPVIEW_BUNDLE_KEY = "AIzaSyAklo5NjMaGF_VYxNi2TS6odpXzjDlr-kg"
    }

    private val viewModel by viewModels<ShoppingCreditMainViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var mMap: GoogleMap

    lateinit var navBar: BottomNavigationView


    /**
     * State event credit
     *
     * To manage the state of the user
     * 0 by default if the user is in good standing and is not in the process of making a request
     * 1 if a loan request is pending
     * 2 if he is overdue
     *
     */
    private var stateEventCredit: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.shopping_credit_main_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_credit_main)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            navBar = requireActivity().findViewById(R.id.nav_view)
            navBar.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(id_map_shopping_bottomsheet)
        //we make invisible the views specific to airtime credit
        try {
            id_airtime_block.visibility = View.GONE
            id_airtime_block.visibility = View.GONE


            askAnyPermission(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                initMap(savedInstanceState)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        observeLiveData()
        viewModel.setStateEvent(ShoppingCreditStateEvent.GetCurrentShoppingCreditLineOfTheUser)
        configureOnClickListener()
    }

    private fun initMap(savedInstanceState: Bundle?) {
        id_shopping_google_map_view.onCreate(savedInstanceState)
        id_shopping_google_map_view.onResume()
        id_shopping_google_map_view.getMapAsync(this)
    }


    private fun configureOnClickListener() {
        id_shopping_credit_lis_store_bottom_sheet.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_shoppingCreditMainFragment_to_parteneredStoresFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        id_shopping_credit_close_bottom_sheet.setOnClickListener {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        id_shopping_google_map_view.setOnClickListener {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

    }

    private fun observeLiveData() {
        viewModel.currentCreditLinetObserver.observe(
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

        viewModel.stateEventCreditObserver.observe(
            viewLifecycleOwner,
            Observer { state ->
                state?.let {
                    stateEventCredit = it
                    try {
                        when (state) {
                            1 -> {
                                //1 if a loan request is pending
                                id_credit_payment_explanation.visibility = View.VISIBLE
                                id_credit_payment_penality_explanation.visibility =
                                    View.GONE
                            }
                            2 -> {
                                //2 if he is overdue

                                id_credit_payment_explanation.visibility = View.GONE
                                id_credit_payment_penality_explanation.visibility =
                                    View.VISIBLE
                            }
                            else -> {
                                // 0 by default if the user is in good standing and is not in the process of making a request
                                id_credit_payment_explanation.visibility = View.VISIBLE
                                id_credit_payment_penality_explanation.visibility =
                                    View.GONE
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(shoppingCreditLineWithShoppingCreditsList: ShoppingCreditLineWithShoppingCreditsList?) {
        if (shoppingCreditLineWithShoppingCreditsList != null) {

            //title
            id_credit_title.textResource = R.string.shopping_credit

            //image
            id_credit_image.imageResource = R.drawable.ic_baseline_shopping_cart


            //credit left
            val creditLeft =
                viewModel.calculateCreditLeft(shoppingCreditLineWithShoppingCreditsList)
            id_credit_left.text =
                creditLeft.formatNumberWithSpaceBetweenThousand() + " F"

            //credit due
            val creditDue = viewModel.calculateCreditDue(shoppingCreditLineWithShoppingCreditsList)
            id_credit_due.text =
                creditDue.formatNumberWithSpaceBetweenThousand() + " F"


            val nowDate = Calendar.getInstance().time

            if (shoppingCreditLineWithShoppingCreditsList.shoppingCreditLine.dueDate < nowDate) {
                //value 2 , because user is overdue
                viewModel.stateEventCreditObserver.value = 2
            }

            //show credit list
            val items = shoppingCreditLineWithShoppingCreditsList.creditList.map {
                ShoppingCreditItem(it, shoppingCreditLineWithShoppingCreditsList.shoppingCreditLine)
            }
            updateRv(items)
        }
    }

    private fun updateRv(items: List<ShoppingCreditItem>) {
        credit_list_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.credit_main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
            R.id.menu_credit_history -> {
                try {
                    findNavController().navigate(R.id.action_shoppingCreditMainFragment_to_shoppingCreditHistoryFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mMap = it
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(0.0, 0.0))
                    .title("Marker")
            )
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stateEventCredit = 0
        try {
            navBar = requireActivity().findViewById(R.id.nav_view)
            navBar.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}