package com.example.gaugeapp.ui.exchange.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.gaugeapp.R
import com.example.gaugeapp.ui.exchange.registration.adapter.RegistrationViewPagerAdapter
import kotlinx.android.synthetic.main.registration_fragment.*
import kotlinx.android.synthetic.main.registration_fragment.view.*

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel

    private lateinit var viewPagerAdapter: RegistrationViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.registration_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_registration)
        setHasOptionsMenu(true)
        viewPager = root.view_pager_registration
        //viewPager.isUserInputEnabled = false
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        listeners()
    }

    private fun initView() {
        viewPagerAdapter = RegistrationViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        viewPager.setPageTransformer { page, position ->
            if (position == 0f) {
                if (viewPager.currentItem == 0) {
                    selectServices()
                } else {
                    selectId()
                }
            }
        }
    }

    private fun listeners() {

        btn_tab_services.setOnClickListener {
            viewPager.setCurrentItem(0, true)
            selectServices()
        }

        btn_tab_id.setOnClickListener {
            viewPager.setCurrentItem(1, true)
            selectId()
        }
    }

    private fun selectServices() {
        btn_tab_services.isEnabled = false
        btn_tab_id.isEnabled = true
    }

    private fun selectId() {
        btn_tab_services.isEnabled = true
        btn_tab_id.isEnabled = false
    }

}