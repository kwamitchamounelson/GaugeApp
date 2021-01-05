package com.example.gaugeapp.ui.communityLoan.yourProfile.id

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import kotlinx.android.synthetic.main.id_fragment.*

class IdFragment : Fragment() {

    companion object {
        fun newInstance() = IdFragment()
    }

    private lateinit var viewModel: IdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.id_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        val image =
            "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"

        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_person_black_24dp)
            .error(R.drawable.ic_person_black_24dp)
            .circleCrop()
            .into(id_profile_user_image)

        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.ic_image_black)
            .error(R.drawable.ic_image_black)
            .centerCrop()
            .into(id_profile_id_cart_front)
    }

}