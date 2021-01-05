package com.example.gaugeapp.ui.communityLoan.commentCampaign

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gaugeapp.R

class CommentCampaignFragment : Fragment() {

    companion object {
        fun newInstance() = CommentCampaignFragment()
    }

    private lateinit var viewModel: CommentCampaignViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.comment_campaign_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommentCampaignViewModel::class.java)
        // TODO: Use the ViewModel
    }

}