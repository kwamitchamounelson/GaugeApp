package com.example.gaugeapp.ui.communityLoan.campaignList

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.campaignList.adapter.CampaignMainViewPagerAdapter
import kotlinx.android.synthetic.main.campaign_lisl_fragment.*
import kotlinx.android.synthetic.main.campaign_lisl_fragment.view.*

class CampaignLislFragment : Fragment() {

    companion object {
        fun newInstance() =
            CampaignLislFragment()
    }

    private lateinit var viewModel: CampaignLislViewModel

    private lateinit var viewPagerAdapter: CampaignMainViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.campaign_lisl_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_campaign_main)
        setHasOptionsMenu(true)
        viewPager = root.view_pager_campaign_main
        viewPager.isUserInputEnabled = false
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
        initView()
        listeners()
    }

    private fun initView() {
        viewPagerAdapter = CampaignMainViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        viewPager.setPageTransformer { page, position ->
            if (position == 0f) {
                if (viewPager.currentItem == 0) {
                    selectAll()
                } else if (viewPager.currentItem == 1) {
                    selectContact()
                } else {
                    selectLend()
                }
            }
        }
    }

    private fun listeners() {

        btn_tab_all_campaign.setOnClickListener {
            viewPager.setCurrentItem(0, true)
            selectAll()
        }

        btn_tab_contact_campaign.setOnClickListener {
            viewPager.setCurrentItem(1, true)
            selectContact()
        }

        btn_tab_lend_campaign.setOnClickListener {
            viewPager.setCurrentItem(2, true)
            selectLend()
        }
    }

    private fun selectAll() {
        btn_tab_all_campaign.isEnabled = false
        btn_tab_contact_campaign.isEnabled = true
        btn_tab_lend_campaign.isEnabled = true
    }

    private fun selectContact() {
        btn_tab_all_campaign.isEnabled = true
        btn_tab_contact_campaign.isEnabled = false
        btn_tab_lend_campaign.isEnabled = true
    }

    private fun selectLend() {
        btn_tab_all_campaign.isEnabled = true
        btn_tab_contact_campaign.isEnabled = true
        btn_tab_lend_campaign.isEnabled = false
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_campaign_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                return true
            }
            R.id.sort_by_amount_campaign -> {
                true
            }
            R.id.filter_campaign_by_guaranteeing -> {
                true
            }
            R.id.filter_campaign_by_highest_first -> {
                true
            }
            R.id.campaign_history -> {
                try {
                    findNavController().navigate(R.id.action_campaignLislFragment_to_loanHistoryFragment)
                }catch (e:Exception){
                    e.printStackTrace()
                }
                true
            }
            R.id.campaign_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}