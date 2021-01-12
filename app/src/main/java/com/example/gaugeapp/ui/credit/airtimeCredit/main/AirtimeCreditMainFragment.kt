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
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.ui.credit.airtimeCredit.borrowAirtimeBottomSheet.BorrowAirtimeBottomSheetFragment
import com.example.gaugeapp.ui.credit.items.AirtimeCreditItem
import com.example.gaugeapp.ui.credit.items.PendingItem
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.formatNumberWithSpaceBetweenThousand
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_credit_main.*
import kotlinx.android.synthetic.main.layout_credit_main.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textResource
import org.jetbrains.anko.toast
import java.util.*

@AndroidEntryPoint
@InternalCoroutinesApi
class AirtimeCreditMainFragment : Fragment() {

    companion object {
        fun newInstance() =
            AirtimeCreditMainFragment()
    }

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
                        }
                    bottomSheet.show(childFragmentManager, "")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeLiveData() {
        viewModel.currentAirtimeCreditLinetObserver.observe(
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
                            else -> {
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
        if (airTimeCreditLine != null) {

            //title
            id_credit_title.textResource = R.string.airtime_credit

            //image
            id_credit_image.imageResource = R.drawable.icon_phone_green

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
        }
    }

    private fun updateRv(items: List<AirtimeCreditItem>) {
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