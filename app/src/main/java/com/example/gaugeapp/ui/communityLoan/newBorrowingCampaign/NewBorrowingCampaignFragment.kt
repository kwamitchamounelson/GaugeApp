package com.example.gaugeapp.ui.communityLoan.newBorrowingCampaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.addGuarantorBottomSheet.AddGuarantorBottomSheetFragment
import com.example.gaugeapp.ui.communityLoan.amountCampaignBottomSheet.AmountCampaignBottomSheetFragment
import com.example.gaugeapp.entities.ENUM_BORROWING_REASON
import com.example.gaugeapp.entities.GuarantorComLoan
import com.example.gaugeapp.ui.communityLoan.interestCampaignBottomSheet.InterestCampaignBottomSheetFragment
import com.example.gaugeapp.ui.communityLoan.items.ImageProfileItem
import com.example.gaugeapp.ui.communityLoan.reasonCampaignBottomSheet.ReasonCampaignBottomSheetFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.new_borrowing_campaign_fragment.*
import kotlinx.android.synthetic.main.new_borrowing_campaign_fragment.view.*
import java.util.*

class NewBorrowingCampaignFragment : Fragment() {

    private var totalPayBack = 0.0
    private var amount = 1000

    private var percentage = 5

    private var borrowingReason: ENUM_BORROWING_REASON? = ENUM_BORROWING_REASON.HEALTH

    private var guarantorList = arrayListOf<GuarantorComLoan>()

    private var paymentDate: Date? = null

    companion object {
        fun newInstance() = NewBorrowingCampaignFragment()
    }

    private lateinit var viewModel: NewBorrowingCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.new_borrowing_campaign_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_new_borrowing_camp)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListner()
        updateUI()
    }

    private fun setOnClickListner() {
        id_borrowing_cam_amount.setOnClickListener {
            val bs = AmountCampaignBottomSheetFragment(amount) { selectedAmount ->
                amount = selectedAmount
                calculateAllAmount()
            }
            bs.show(childFragmentManager, "")
        }

        id_borrowing_cam_interest.setOnClickListener {
            val bs = InterestCampaignBottomSheetFragment(amount, percentage) { selectedInterest ->
                percentage = selectedInterest
                calculateAllAmount()
            }
            bs.show(childFragmentManager, "")
        }

        id_borrowing_cam_add_reason.setOnClickListener {
            val bs = ReasonCampaignBottomSheetFragment(borrowingReason) { selectedReason ->
                borrowingReason = selectedReason
                updateReason()
            }
            bs.show(childFragmentManager, "")
        }

        id_borrowing_cam_add_guarantor.setOnClickListener {
            val bs = AddGuarantorBottomSheetFragment(guarantorList) { list ->
                guarantorList = list
                loadGuarantors()
            }
            bs.show(childFragmentManager, "")
        }

        id_borrowing_cam_payment_date.setOnClickListener { }
    }

    private fun updateReason() {
        if (borrowingReason != null) {
            id_borrowing_reason.text = borrowingReason?.reason
        } else {
            id_borrowing_reason.visibility = View.INVISIBLE
        }
    }

    private fun updateUI() {
        calculateAllAmount()

        loadGuarantors()
    }

    private fun loadGuarantors() {
        //val image = "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb"

        val items = guarantorList.map {
            ImageProfileItem(it.imageUrl)
        }

        id_borrowing_cam_guarantor_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }
    }

    private fun calculateAllAmount() {
        id_borrowing_cam_amount.text = "$amount F"

        id_borrowing_cam_interest.text = "$percentage%"

        val interestAmount = (amount * (percentage / 100.0))

        id_borrowing_cam_interest_amount.text = "${interestAmount.toInt()} F"

        totalPayBack = amount + interestAmount
        id_borrowing_cam_total_pay_back.text = "${totalPayBack.toInt()} F"
    }

}