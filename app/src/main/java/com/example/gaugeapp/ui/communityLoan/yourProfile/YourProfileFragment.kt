package com.example.gaugeapp.ui.communityLoan.yourProfile

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.communityLoan.yourProfile.adapter.YourProfileViewPagerAdapter
import kotlinx.android.synthetic.main.your_profile_fragment.*
import kotlinx.android.synthetic.main.your_profile_fragment.view.*

class YourProfileFragment : Fragment() {

    companion object {
        fun newInstance() = YourProfileFragment()
    }

    private lateinit var viewModel: YourProfileViewModel

    private lateinit var viewPagerAdapter: YourProfileViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.your_profile_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_your_profile)
        setHasOptionsMenu(true)
        viewPager = root.view_pager_your_profile
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
        viewPagerAdapter = YourProfileViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        viewPager.setPageTransformer { page, position ->
            if (position == 0f) {
                if (viewPager.currentItem == 0) {
                    selectAboutYou()
                } else {
                    selectId()
                }
            }
        }
    }

    private fun listeners() {

        btn_tab_about_you.setOnClickListener {
            viewPager.setCurrentItem(0, true)
            selectAboutYou()
        }

        btn_tab_id.setOnClickListener {
            viewPager.setCurrentItem(1, true)
            selectId()
        }
    }

    private fun selectAboutYou() {
        btn_tab_about_you.isEnabled = false
        btn_tab_id.isEnabled = true
    }

    private fun selectId() {
        btn_tab_about_you.isEnabled = true
        btn_tab_id.isEnabled = false
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_your_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                return true
            }
            R.id.edit_profile -> {
                try {
                    //findNavController().navigate(R.id.action_campaignLislFragment_to_guaranteeingFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}