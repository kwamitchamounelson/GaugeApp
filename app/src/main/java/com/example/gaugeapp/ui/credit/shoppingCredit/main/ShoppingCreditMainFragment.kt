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
import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.ui.credit.items.PendingItem
import com.example.gaugeapp.ui.credit.items.ShoppingCreditItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.example.gaugeapp.utils.permissionsutils.FragmentPermissions
import com.example.gaugeapp.utils.permissionsutils.askAnyPermission
import com.example.gaugeapp.utils.printLogD
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
import org.jetbrains.anko.*
import java.util.*

private const val TAG = "ShoppingCreditMainFragm"

@AndroidEntryPoint
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class ShoppingCreditMainFragment : FragmentPermissions(), OnMapReadyCallback {

    companion object {
        fun newInstance() =
            ShoppingCreditMainFragment()

        private const val MAPVIEW_BUNDLE_KEY = "AIzaSyAklo5NjMaGF_VYxNi2TS6odpXzjDlr-kg"
    }

    private var firstTime: Boolean = true
    private var creditDue: Double = 0.0
    private var creditLeft: Double = 0.0
    private var currentShoppingCreditLine: ShoppingCreditLine? = null
    private var currentShoppingCreditRequest: ShoppingCreditRequest? = null

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
    private var pending = false

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
            id_pending_block.visibility = View.GONE
            id_pending_block.visibility = View.GONE
            id_credit_borrow_text_button.visibility = View.GONE

            //title
            id_credit_title.textResource = R.string.shopping_credit

            //image
            id_credit_image.imageResource = R.drawable.ic_baseline_shopping_cart

            credit_progress.visibility = View.VISIBLE
            credit_list_empty.visibility = View.GONE
            id_credit_list_data_block.visibility = View.GONE

            setUiStateGoodStandingNotRequest()


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

        id_shopping_credit_drawer_bottom_sheet.setOnClickListener {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

    }


    private fun observeLiveData() {

        viewModel.shoppingCreditRequestObserver.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    currentShoppingCreditRequest = dataState.data
                    printLogD(
                        TAG,
                        "currentShoppingCreditRequest => ${currentShoppingCreditRequest.toString()}"
                    )
                    //to do pending animation
                    if (currentShoppingCreditRequest != null && currentShoppingCreditRequest!!.requestEnable) {
                        when (currentShoppingCreditRequest!!.status) {
                            ENUM_REQUEST_STATUS.PENDING -> {
                                setUiStatePendingRequest()
                            }
                            ENUM_REQUEST_STATUS.REJECTED -> {
                                //kola rejected the request, we show and alert to notify the user
                                requireContext().alert {
                                    titleResource = R.string.app_name
                                    messageResource = R.string.your_last_request_has_been_rejected
                                    okButton {
                                    }
                                    onCancelled {
                                        setUiStateGoodStandingNotRequest()
                                        ShoppingCreditStateEvent.CloseShoppingCreditRequest(
                                            currentShoppingCreditRequest!!
                                        )
                                    }
                                }.show()
                            }
                            ENUM_REQUEST_STATUS.VALIDATED -> {
                                //we add Shopping credit to corresponding to this request to the current credit line and we disable de request
                                setUiStateGoodStandingNotRequest()
                                currentShoppingCreditRequest!!.requestEnable = false
                                viewModel.setStateEvent(
                                    ShoppingCreditStateEvent.CloseValidatedShoppingCreditRequest(
                                        currentShoppingCreditLine!!,
                                        currentShoppingCreditRequest!!
                                    )
                                )
                            }
                            ENUM_REQUEST_STATUS.CANCELED -> {
                                //We only disable de request
                                setUiStateGoodStandingNotRequest()
                                viewModel.setStateEvent(
                                    ShoppingCreditStateEvent.CloseShoppingCreditRequest(
                                        currentShoppingCreditRequest!!
                                    )
                                )
                            }
                        }
                    }
                }
                is DataState.Failure -> {
                    requireContext().toast(R.string.connection_error_please_try_again)
                    dataState.throwable?.printStackTrace()
                }
            }
        })

        viewModel.currentShoppingCreditLinetObserver.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        credit_progress.visibility = View.VISIBLE
                    }
                    is DataState.Success -> {
                        credit_progress.visibility = View.GONE
                        currentShoppingCreditLine = dataState.data
                        printLogD(
                            TAG,
                            "currentShoppingCreditLine => ${currentShoppingCreditLine.toString()}"
                        )
                        if (currentShoppingCreditLine != null) {
                            if (currentShoppingCreditLine!!.id.isNotBlank()) {

                                val totalLoan =
                                    currentShoppingCreditLine!!.shoppingCreditList.sumByDouble { it.amount }

                                val maxAmount =
                                    currentShoppingCreditLine!!.maxAmountToLoan

                                if (totalLoan < maxAmount) {
                                    updateUI(currentShoppingCreditLine!!)
                                } else if (totalLoan == maxAmount) {
                                    val totalUnSolved =
                                        currentShoppingCreditLine!!.shoppingCreditList.filter { !it.solved }
                                            .sumByDouble {
                                                it.amount
                                            }
                                    if (totalUnSolved == 0.0) {
                                        //the current credit line is solved, we close it and automatically new is created
                                        viewModel.setStateEvent(
                                            ShoppingCreditStateEvent.CloseCurentCreditLine(
                                                currentShoppingCreditLine!!
                                            )
                                        )
                                    } else {
                                        updateUI(currentShoppingCreditLine!!)
                                    }
                                }

                            } else {
                                //we try to recover the credit line to be sure to have the id
                                viewModel.setStateEvent(ShoppingCreditStateEvent.GetCurrentShoppingCreditLineOfTheUser)
                            }
                        } else {
                            //credit left
                            id_credit_left.text = "0 F"

                            //credit due
                            id_credit_due.text = "0 F"

                            //init new airtime credit line
                            viewModel.setStateEvent(ShoppingCreditStateEvent.InitShoppingCreditLine)
                        }
                    }
                    is DataState.Failure -> {
                        dataState.throwable?.printStackTrace()
                        credit_progress.visibility = View.GONE
                    }
                }
            })
    }


    private fun setUiStateOverdue() {
        pending = false
        //2 if he is overdue

        id_pending_block.visibility = View.GONE

        id_credit_payment_explanation.visibility = View.GONE
        id_credit_payment_penality_explanation.visibility =
            View.VISIBLE
    }

    private fun setUiStatePendingRequest() {
        pending = true
        //1 if a loan request is pending
        id_credit_borrow_text_state.apply {
            visibility = View.VISIBLE
            textResource = R.string.you_are_on_the_way
            textColorResource = R.color.colorPrimary
        }
        id_credit_borrow_grace_periode.visibility = View.GONE
        id_credit_borrow_pending_rv.visibility = View.VISIBLE
        id_credit_borrow_pending_cancel.visibility = View.VISIBLE
        id_credit_payment_explanation.visibility = View.VISIBLE
        id_credit_payment_penality_explanation.visibility =
            View.GONE
        startPendingAnimation()
    }

    private fun setUiStateGoodStandingNotRequest() {
        pending = false
        // 0 by default if the user is in good standing and is not in the process of making a request
        id_pending_block.visibility = View.GONE

        id_credit_payment_penality_explanation.visibility =
            View.VISIBLE
    }

    private fun startPendingAnimation() {
        if (pending) {

            val pendingItems = (0..3).map {
                PendingItem(it)
            }

            id_credit_borrow_pending_rv.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = GroupAdapter<ViewHolder>().apply {
                    add(Section(pendingItems))
                }
            }

            val pendingCount = (pendingItems.size - 1)

            //start thread animation
            try {
                var currentIndex = 0
                val thread = Thread {
                    while (pending) {
                        requireActivity().runOnUiThread(Runnable {
                            pendingItems.forEach { item ->
                                item.apply {
                                    isCurrentPending = (currentIndex == item.number)
                                    notifyChanged()
                                }
                            }
                            currentIndex++
                            if (currentIndex > pendingCount) {
                                currentIndex = 0
                            }
                        })
                        try {
                            Thread.sleep(200)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                thread.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(shoppingCreditLine: ShoppingCreditLine) {
        //credit left
        creditLeft = viewModel.calculateCreditLeft(shoppingCreditLine)
        id_credit_left.text =
            creditLeft.formatNumberWithSpaceBetweenThousand() + " F"

        //credit due
        creditDue = viewModel.calculateCreditDue(shoppingCreditLine)
        id_credit_due.text =
            creditDue.formatNumberWithSpaceBetweenThousand() + " F"


        val nowDate = Calendar.getInstance().time

        if (shoppingCreditLine.dueDate < nowDate) {
            //value 2 , because user is overdue
            setUiStateOverdue()
        }

        //show credit list
        val items = shoppingCreditLine.shoppingCreditList.map {
            ShoppingCreditItem(it, shoppingCreditLine)
        }
        updateRv(items)

        if (firstTime) {
            firstTime = false
            //we fetch if we have pending request
            viewModel.setStateEvent(
                ShoppingCreditStateEvent.GetLastShoppingCreditRequest(
                    currentShoppingCreditLine!!.id
                )
            )
        }
    }

    private fun updateRv(items: List<ShoppingCreditItem>) {
        credit_list_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }

        if (items.isEmpty()) {
            credit_list_empty.visibility = View.VISIBLE
            id_credit_list_data_block.visibility = View.GONE
        } else {
            credit_list_empty.visibility = View.GONE
            id_credit_list_data_block.visibility = View.VISIBLE
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
        pending = false
        try {
            navBar = requireActivity().findViewById(R.id.nav_view)
            navBar.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}