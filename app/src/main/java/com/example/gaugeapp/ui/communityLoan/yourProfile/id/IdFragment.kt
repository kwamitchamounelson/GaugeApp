package com.example.gaugeapp.ui.communityLoan.yourProfile.id

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gaugeapp.R
import kotlinx.android.synthetic.main.id_fragment.*

class IdFragment : Fragment() {

    companion object {
        fun newInstance() = IdFragment()
    }

    private lateinit var viewModel: IdViewModel

    private var imageUriIdCardFront: Uri? = null
    private var imageUriIdCardBack: Uri? = null
    private val REQUEST_OPEN_CAMERA = 3776
    private val REQUEST_PERMISSION_OPEN_CAMERA = 397

    private var isFront: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.id_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()
        setOnClicListener()
    }

    private fun setOnClicListener() {
        id_your_profile_id_card_front_parent_block.setOnClickListener {
            findNavController().navigate(R.id.action_yourProfileFragment_to_cameraIdCardFragment)
        }

        id_your_profile_id_card_back_parent_block.setOnClickListener {
            findNavController().navigate(R.id.action_yourProfileFragment_to_cameraIdCardFragment)
        }

        id_your_profile_id_card_front_parent_block.setOnClickListener {
            isFront = true
            openCamera()
        }

        id_your_profile_id_card_back_parent_block.setOnClickListener {
            isFront = false
            openCamera()
        }
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

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION_OPEN_CAMERA
            )
        } else {
            selectImageFromCamera()
        }
    }

    private fun selectImageFromCamera() {
        isFront?.let {
            if (it) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
                imageUriIdCardFront =
                    requireContext().contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )
                //camera intent
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriIdCardFront)
                startActivityForResult(cameraIntent, REQUEST_OPEN_CAMERA)
            } else {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
                imageUriIdCardBack =
                    requireContext().contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )
                //camera intent
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriIdCardBack)
                startActivityForResult(cameraIntent, REQUEST_OPEN_CAMERA)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_OPEN_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromCamera()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OPEN_CAMERA && resultCode == Activity.RESULT_OK) {
            showImageSelected()
        }
    }

    private fun showImageSelected() {
        isFront?.let {
            imageUriIdCardFront?.let {
                id_text_front_id_card.visibility = View.GONE
                id_profile_id_cart_front.visibility = View.VISIBLE
                Glide.with(this)
                    .load(imageUriIdCardFront)
                    .placeholder(R.drawable.ic_image_black)
                    .error(R.drawable.ic_image_black)
                    .centerCrop()
                    .into(id_profile_id_cart_front)
            }


            imageUriIdCardBack?.let {
                id_text_back_id_card.visibility = View.GONE
                id_profile_id_cart_back.visibility = View.VISIBLE
                Glide.with(this)
                    .load(imageUriIdCardBack)
                    .placeholder(R.drawable.ic_image_black)
                    .error(R.drawable.ic_image_black)
                    .centerCrop()
                    .into(id_profile_id_cart_back)
            }
        }
    }

}