package com.example.gaugeapp.campaignDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import kotlinx.android.synthetic.main.campaign_detail_fragment.*

class CampaignDetailFragment : Fragment() {

    companion object {
        fun newInstance() = CampaignDetailFragment()
    }

    private lateinit var viewModel: CampaignDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.campaign_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CampaignDetailViewModel::class.java)
        updateUI()
    }

    private fun updateUI() {
        val image1 =
            "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb"

        val image2 =
            "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"

        val image = listOf(image1, image2).random()

        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_user_profile)


        //load guarantors images
        Glide.with(this)
            .load(image1)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_guarantor_image_1)

        Glide.with(this)
            .load(image2)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_guarantor_image_2)

        Glide.with(this)
            .load(image1)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_guarantor_image_3)

        Glide.with(this)
            .load(image2)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_guarantor_image_4)


        //load lenders images
        Glide.with(this)
            .load(image1)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_lender_image_1)

        Glide.with(this)
            .load(image2)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_lender_image_2)

        Glide.with(this)
            .load(image1)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_lender_image_3)

        Glide.with(this)
            .load(image2)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_campaign_detail_lender_image_4)

    }

}