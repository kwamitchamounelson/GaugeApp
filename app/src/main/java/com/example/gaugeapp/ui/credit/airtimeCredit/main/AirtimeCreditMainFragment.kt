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

    private var firstTime: Boolean = true
    private var creditDue: Double = 0.0
    private var creditLeft: Double = 0.0
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
    private var pending = false

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
            setUiStateGoodStandingNotRequest()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        observeLiveData()
        viewModel.setStateEvent(AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser)
        configureOnClickListener()
    }

    private fun configureOnClickListener() {
        id_credit_borrow_text_button.setOnClickListener {
            val bottomSheet =
                BorrowAirtimeBottomSheetFragment { amount, phoneNumber ->
                    if (currentAirtimeCreditLine != null) {
                        if (creditLeft >= amount) {
                            requireContext().toast("$amount,  To $phoneNumber")
                            setUiStatePendingRequest()
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
                                AirtimeCreditStateEvent.RequestBorrowAirtimeCredit(
                                    airtimeCreditRequest
                                )
                            )
                        } else {
                            requireContext().toast(R.string.your_remaining_credit_is_insufficient)
                        }
                    } else {
                        //current credit line is null, we try to get it on the server
                        requireContext().toast(R.string.connection_error_please_try_again)
                        viewModel.setStateEvent(AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser)
                    }
                }
            bottomSheet.show(childFragmentManager, "")
        }

        id_credit_borrow_pending_cancel.setOnClickListener {
            setUiStateGoodStandingNotRequest()
            viewModel.setStateEvent(
                AirtimeCreditStateEvent.CancelCloselAirtimeCreditRequest(
                    currentAirtimeCreditRequest!!
                )
            )
        }
    }

    private fun observeLiveData() {

        viewModel.airtimeCreditRequestObserver.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    currentAirtimeCreditRequest = dataState.data
                    printLogD(
                        TAG,
                        "currentAirtimeCreditRequest => ${currentAirtimeCreditRequest.toString()}"
                    )
                    //to do pending animation
                    if (currentAirtimeCreditRequest != null && currentAirtimeCreditRequest!!.requestEnable) {
                        when (currentAirtimeCreditRequest!!.status) {
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
                                        AirtimeCreditStateEvent.CloseAirtimeCreditRequest(
                                            currentAirtimeCreditRequest!!
                                        )
                                    }
                                }.show()
                            }
                            ENUM_REQUEST_STATUS.VALIDATED -> {
                                //we add airtime credit to corresponding to this request to the current credit line and we disable de request
                                setUiStateGoodStandingNotRequest()
                                currentAirtimeCreditRequest!!.requestEnable = false
                                viewModel.setStateEvent(
                                    AirtimeCreditStateEvent.CloseValidatedAirtimeCreditRequest(
                                        currentAirtimeCreditLine!!,
                                        currentAirtimeCreditRequest!!
                                    )
                                )
                            }
                            ENUM_REQUEST_STATUS.CANCELED -> {
                                //We only disable de request
                                setUiStateGoodStandingNotRequest()
                                viewModel.setStateEvent(
                                    AirtimeCreditStateEvent.CloseAirtimeCreditRequest(
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
                        currentAirtimeCreditLine = dataState.data
                        printLogD(
                            TAG,
                            "currentAirtimeCreditLine => ${currentAirtimeCreditLine.toString()}"
                        )
                        if (currentAirtimeCreditLine != null) {
                            if (currentAirtimeCreditLine!!.id.isNotBlank()) {

                                val totalLoan =
                                    currentAirtimeCreditLine!!.airtimeCreditList.sumByDouble { it.amount }

                                val maxAmount =
                                    currentAirtimeCreditLine!!.maxAmountToLoan

                                if (totalLoan < maxAmount) {
                                    updateUI(currentAirtimeCreditLine!!)
                                } else if (totalLoan == maxAmount) {
                                    val totalUnSolved =
                                        currentAirtimeCreditLine!!.airtimeCreditList.filter { !it.solved }
                                            .sumByDouble {
                                                it.amount
                                            }
                                    if (totalUnSolved == 0.0) {
                                        //the current credit line is solved, we close it and automatically new is created
                                        viewModel.setStateEvent(
                                            AirtimeCreditStateEvent.CloseCurentCreditLine(
                                                currentAirtimeCreditLine!!
                                            )
                                        )
                                    } else {
                                        updateUI(currentAirtimeCreditLine!!)
                                    }
                                }

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
        id_credit_borrow_text_button.visibility = View.GONE
        id_credit_borrow_text_state.apply {
            visibility = View.VISIBLE
            textResource = R.string.you_are_overdue
            textColorResource = R.color.color_red_credit_due
        }
        id_credit_borrow_grace_periode.visibility = View.VISIBLE
        id_credit_borrow_pending_rv.visibility = View.GONE
        id_credit_borrow_pending_cancel.visibility = View.GONE

        id_credit_payment_explanation.visibility = View.GONE
        id_credit_payment_penality_explanation.visibility =
            View.VISIBLE
    }

    private fun setUiStatePendingRequest() {
        pending = true
        //1 if a loan request is pending
        id_credit_borrow_text_button.visibility = View.GONE
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
        id_credit_borrow_text_button.visibility = View.VISIBLE
        id_credit_borrow_text_state.visibility = View.GONE
        id_credit_borrow_grace_periode.visibility = View.GONE
        id_credit_borrow_pending_rv.visibility = View.GONE
        id_credit_borrow_pending_cancel.visibility = View.GONE
        id_credit_payment_explanation.visibility = View.VISIBLE
        id_credit_payment_penality_explanation.visibility =
            View.GONE
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
    private fun updateUI(airTimeCreditLine: AirTimeCreditLine) {
        //credit left
        creditLeft = viewModel.calculateCreditLeft(airTimeCreditLine)
        id_credit_left.text =
            creditLeft.formatNumberWithSpaceBetweenThousand() + " F"

        //credit due
        creditDue = viewModel.calculateCreditDue(airTimeCreditLine)
        id_credit_due.text =
            creditDue.formatNumberWithSpaceBetweenThousand() + " F"


        val nowDate = Calendar.getInstance().time

        if (airTimeCreditLine.dueDate < nowDate) {
            //value 2 , because user is overdue
            setUiStateOverdue()
        }

        //show credit list
        val items = airTimeCreditLine.airtimeCreditList.map {
            AirtimeCreditItem(it, airTimeCreditLine)
        }
        updateRv(items)

        if (firstTime) {
            firstTime = false
            //we fetch if we have pending request
            viewModel.setStateEvent(
                AirtimeCreditStateEvent.GetLastAirtimeCreditRequest(
                    currentAirtimeCreditLine!!.id
                )
            )
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
        pending = false
    }

}