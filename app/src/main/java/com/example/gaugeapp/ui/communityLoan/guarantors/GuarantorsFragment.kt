package com.example.gaugeapp.ui.communityLoan.guarantors

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.GuarantorComLoan
import com.example.gaugeapp.ui.communityLoan.items.GuarantorItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.guarantors_fragment.*
import kotlinx.android.synthetic.main.guarantors_fragment.view.*

class GuarantorsFragment : Fragment() {

    companion object {
        fun newInstance() = GuarantorsFragment()
    }

    private var guarantorsList = arrayListOf<GuarantorComLoan>()
    private lateinit var viewModel: GuarantorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.guarantors_fragment, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.my_toolbar_guarantors)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        guarantorsList = getAllGuarantors()

        id_toolbar_guarantors_count.text = guarantorsList.size.toString()

        id_guarantor_list_avg.text = guarantorsList.map { it.score }.average().toInt().toString()

        val items = guarantorsList.map { GuarantorItem(it) }

        id_guarantor_list_rv.apply {
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = GroupAdapter<ViewHolder>().apply {
                add(Section(items))
            }
        }

    }

    private fun getAllGuarantors(): ArrayList<GuarantorComLoan> {
        //TODO to remove
        val list = arrayListOf<GuarantorComLoan>()
        (0..10).forEach { _ ->
            val image = listOf(
                "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/USER_PROFILS%2F7MdBbt53EIQeAyIpGXkJaaCTEjb2?alt=media&token=0d2ed20f-a261-4f0a-a17e-f0c19671ebfb",
                "https://firebasestorage.googleapis.com/v0/b/kola-application.appspot.com/o/USER_PROFILS%2F3hV4TKs15ffQ8MeIT0lPV9Ao2vl1?alt=media&token=ccb7acb8-f7df-4ca0-bcf8-f50e29b9c475"
            ).random()

            val scoreRandom = (0..100).random().toDouble()

            val name = listOf("Arthuro Oss", "Sashila Hax", "Boris Bean").random()

            val guarantor = GuarantorComLoan().apply {
                imageUrl = image
                score = scoreRandom
                userFullName = name
            }
            list.add(guarantor)
        }
        return list
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_guarantors, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}