package com.example.gaugeapp.ui.credit.airtimeCredit.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.ui.credit.airtimeCredit.borrowAirtimeBottomSheet.BorrowAirtimeBottomSheetFragment
import com.example.gaugeapp.ui.credit.items.AirtimeCreditItem
import com.example.gaugeapp.ui.credit.items.PendingItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.example.gaugeapp.utils.printLogD
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_credit_main.*
import kotlinx.android.synthetic.main.layout_credit_main.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.*
import java.util.*


private const val TAG = "AirtimeCreditMainFragme"

@AndroidEntryPoint
@InternalCoroutinesApi
class AirtimeCreditMainFragment : Fragment() {

    companion object {
        fun newInstance() =
            AirtimeCreditMainFragment()
    }

    private var currentAirtimeCreditLine: AirTimeCreditLine? = null
    private var currentAirtimeCreditRequest: AirtimeCreditRequest? = null
    private val viewModel by viewModels<AirtimeCreditMainViewModel>()

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
        val root = inflater.inflate(R.layout.airtime_credit_main_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_credit_main)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            credit_progress.visibility = View.VISIBLE

            //title
            id_credit_title.textResource = R.string.airtime_credit

            //image
            id_credit_image.imageResource = R.drawable.icon_phone_green

            credit_list_empty.visibility = View.GONE
            id_credit_list_data_block.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        observeLiveData()
        viewModel.setStateEvent(AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser)
        configureOnClickListener()
    }

    private fun configureOnClickListener() {
        try {
            id_credit_borrow_button.setOnClickListener {
                if (stateEventCredit == 0) {
                    val bottomSheet =
                        BorrowAirtimeBottomSheetFragment { amount, phoneNumber ->
                            requireContext().toast("$amount,  To $phoneNumber")
                            viewModel.stateEventCreditObserver.value = 1
                            if (currentAirtimeCreditLine != null) {
                                //we launch de request to borrow airtime
                                val nowDate = Calendar.getInstance().time
                                val airtimeCreditRequest = AirtimeCreditRequest(
                                    "",
                                    currentAirtimeCreditLine!!.id,
                                    amount.toDouble(),
                                    phoneNumber,
                                    viewModel.userId,
                                    nowDate,
                                    ENUM_REQUEST_STATUS.PENDING,
                                    nowDate,
                                    true
                                )
                                viewModel.setStateEvent(
                                    AirtimeCreditStateEvent.RequestBorrowAirtimeCredit(airtimeCreditRequest)
                                )
                            } else {
                                //current credit line is null, we try to get it on the server
                                requireContext().toast(R.string.connection_error_please_try_again)
                                viewModel.setStateEvent(AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser)
                            }
                        }
                    bottomSheet.show(childFragmentManager, "")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeLiveData() {

        viewModel.airtimeCreditRequestObserver.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    currentAirtimeCreditRequest = dataState.data
                    printLogD(TAG, "currentAirtimeCreditRequest => ${dataState.data.toString()}")
                    //to do pending animation
                    if (dataState.data != null && dataState.data.enable) {
                        when (dataState.data.status) {
                            ENUM_REQUEST_STATUS.PENDING -> {
                                if (viewModel.stateEventCreditObserver.value != 1) {
                                    viewModel.stateEventCreditObserver.value = 1
                                }
                            }
                            ENUM_REQUEST_STATUS.REJECTED -> {
                                if (viewModel.stateEventCreditObserver.value != 3) {
                                    viewModel.stateEventCreditObserver.value = 3
                                }
                            }
                            ENUM_REQUEST_STATUS.VALIDATED -> {
                                //we add airtime credit to corresponding to this request to the current credit line and we disable de request
                                viewModel.setStateEvent(
                                    AirtimeCreditStateEvent.ValidateAirtimeCreditRequest(
                                        currentAirtimeCreditLine!!,
                                        currentAirtimeCreditRequest!!
                                    )
                                )
                            }
                            ENUM_REQUEST_STATUS.CANCELED -> {
                                //We only disable de request
                                viewModel.setStateEvent(
                                    AirtimeCreditStateEvent.DisableAirtimeCreditRequest(
                                        currentAirtimeCreditRequest!!
                                    )
                                )
                            }
                        }
                    }
                }
                is DataState.Failure -> {
                    requireContext().toast(R.string.connection_error_please_try_again)
                    dataState.throwable?.printStackTrace()
                    updateUI(currentAirtimeCreditLine)
                }
            }
        })

        viewModel.currentAirtimeCreditLinetObserver.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        credit_progress.visibility = View.VISIBLE
                    }
                    is DataState.Success -> {
                        credit_progress.visibility = View.GONE
                        printLogD(TAG, "currentAirtimeCreditLine => ${dataState.data.toString()}")
                        updateUI(dataState.data)
                    }
                    is DataState.Failure -> {
                        dataState.throwable?.printStackTrace()
                        credit_progress.visibility = View.GONE
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
                            0 -> {
                                // 0 by default if the user is in good standing and is not in the process of making a request
                                id_credit_borrow_state_text.apply {
                                    textResource = R.string.borrow_airtime
                                    textColorResource = R.color.colorPrimary
                                }
                                id_credit_borrow_grace_periode.visibility = View.GONE
                                id_credit_borrow_pending_rv.visibility = View.GONE
                                id_credit_payment_explanation.visibility = View.VISIBLE
                                id_credit_payment_penality_explanation.visibility =
                                    View.GONE
                            }
                            1 -> {
                                //1 if a loan request is pending
                                id_credit_borrow_state_text.apply {
                                    textResource = R.string.you_are_on_the_way
                                    textColorResource = R.color.colorPrimary
                                }
                                id_credit_borrow_grace_periode.visibility = View.GONE
                                id_credit_borrow_pending_rv.visibility = View.VISIBLE
                                id_credit_payment_explanation.visibility = View.VISIBLE
                                id_credit_payment_penality_explanation.visibility =
                                    View.GONE
                                startPendingAnimation()
                            }
                            2 -> {
                                //2 if he is overdue
                                id_credit_borrow_state_text.apply {
                                    textResource = R.string.you_are_overdue
                                    textColorResource = R.color.color_red_credit_due
                                }
                                id_credit_borrow_grace_periode.visibility = View.VISIBLE
                                id_credit_borrow_pending_rv.visibility = View.GONE

                                id_credit_payment_explanation.visibility = View.GONE
                                id_credit_payment_penality_explanation.visibility =
                                    View.VISIBLE
                            }
                            3 -> {
                                //kola rejected the request, we show and alert to notify the user
                                requireContext().alert {
                                    titleResource = R.string.app_name
                                    messageResource = R.string.your_last_request_has_been_rejected
                                    okButton {
                                        AirtimeCreditStateEvent.DisableAirtimeCreditRequest(
                                            currentAirtimeCreditRequest!!
                                        )
                                    }
                                    onCancelled {
                                        AirtimeCreditStateEvent.DisableAirtimeCreditRequest(
                                            currentAirtimeCreditRequest!!
                                        )
                                    }
                                }.show()
                            }
                            else -> {

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun startPendingAnimation() {
        if (stateEventCredit == 1) {

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

            //start thread animation
            try {
                var currentIndex = 0
                val thread = Thread {
                    while (stateEventCredit == 1) {
                        requireActivity().runOnUiThread(Runnable {
                            pendingItems.forEach { item ->
                                item.apply {
                                    isCurrentPending = (currentIndex == item.number)
                                    notifyChanged()
                                }
                            }
                            currentIndex++
                            if (currentIndex > 3) {
                                currentIndex = 0
                            }
                        })
                        try {
                            Thread.sleep(500)
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
    private fun updateUI(airTimeCreditLine: AirTimeCreditLine?) {
        currentAirtimeCreditLine = airTimeCreditLine
        if (airTimeCreditLine != null) {
            if (airTimeCreditLine.id.isNotBlank()) {
                //credit left
                val creditLeft = viewModel.calculateCreditLeft(airTimeCreditLine)
                id_credit_left.text =
                    creditLeft.formatNumberWithSpaceBetweenThousand() + " F"

                //credit due
                val creditDue = viewModel.calculateCreditDue(airTimeCreditLine)
                id_credit_due.text =
                    creditDue.formatNumberWithSpaceBetweenThousand() + " F"


                val nowDate = Calendar.getInstance().time

                if (airTimeCreditLine.dueDate < nowDate) {
                    //value 2 , because user is overdue
                    viewModel.stateEventCreditObserver.value = 2
                }

                //show credit list
                val items = airTimeCreditLine.airtimeCreditList.map {
                    AirtimeCreditItem(it, airTimeCreditLine)
                }
                updateRv(items)

                //we fetch if we have pending request
                viewModel.setStateEvent(
                    AirtimeCreditStateEvent.GetLastAirtimeCreditRequest(
                        airTimeCreditLine.id
                    )
                )

            } else {
                //we try to recover the credit line to be sure to have the id
                viewModel.setStateEvent(AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser)
            }
        } else {
            //credit left
            id_credit_left.text = "0 F"

            //credit due
            id_credit_due.text = "0 F"

            //init new airtime credit line
            viewModel.setStateEvent(AirtimeCreditStateEvent.InitAirtimeCreditLine)
        }
    }

    private fun updateRv(items: List<AirtimeCreditItem>) {
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
                    findNavController().navigate(R.id.action_airtimeCreditMainFragment_to_airtimeCreditHistoryFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        stateEventCredit = 0
    }

}