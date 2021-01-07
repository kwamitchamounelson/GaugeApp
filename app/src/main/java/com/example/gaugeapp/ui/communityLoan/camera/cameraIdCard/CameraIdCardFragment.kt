package com.example.gaugeapp.ui.communityLoan.camera.cameraIdCard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.gaugeapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.camera_id_card_fragment.*
import org.jetbrains.anko.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraIdCardFragment : Fragment() {

    lateinit var navBar: BottomNavigationView

    private val ALL_PERMISSION_REQUEST = 12

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_id_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateUI()

        CameraX.unbindAll()
        ShowPreviewCamera()
    }

    private fun updateUI() {
        try {
            navBar = requireActivity().findViewById(R.id.nav_view)
            navBar.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ShowPreviewCamera() {

        if ((ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED)
            || (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                ALL_PERMISSION_REQUEST
            )
        } else {

            try {
                textureView.post {
                    startCamera()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        // Every time the provided texture view changes, recompute layout
        try {
            textureView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                updateTransform()
            }

        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    private fun startCamera() {
        CameraX.unbindAll()

        //====================== Image Preview Config code Start==========================
        // Create configuration object for the viewfinder use case
        try {
            val aspectratio = Rational(textureView.width, textureView.height)
            val screen = Size(textureView.width, textureView.height)

            val pConfig =
                PreviewConfig.Builder().setTargetAspectRatio(aspectratio)
                    .setTargetResolution(screen)
                    .build()
            val preview = Preview(pConfig)

            // Every time the viewfinder is updated, recompute layout
            preview.setOnPreviewOutputUpdateListener {
                // To update the SurfaceTexture, we have to remove it and re-add it
                try {
                    val parent = textureView.parent as ViewGroup
                    parent.removeView(textureView)
                    parent.addView(textureView, 0)
                    textureView.setSurfaceTexture(it.surfaceTexture)
                    updateTransform()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


            }
            //====================== Image Preview Config code End==========================

            val imageCaptureConfig =
                ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                    .setTargetRotation(activity?.windowManager?.defaultDisplay!!.rotation).build()


            // Build the viewfinder use case
            val imageCapture = ImageCapture(imageCaptureConfig)

            id_camera_button_id_card.setOnClickListener {

                val root = Environment.getExternalStorageDirectory().toString();

                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val fname = "kola_$timeStamp.jpg";

                val file = File(root, fname)
                imageCapture.takePicture(file, object : ImageCapture.OnImageSavedListener {
                    override fun onImageSaved(file: File) {
                        val msg = "Photo capture succeeded: ${file.absolutePath}"
                        // context!!.toast(msg)
                        //context!!.toast("Pic capture at ${file.absolutePath}")

                        val bitmap = BitmapFactory.decodeFile(file.path)
                        val rotateBitmap = rotateImg(bitmap)


                        val imageUrl = rotateBitmap?.let {
                            if (isExternalStorageWritable()) {
                                //saveImage(it, activity!!);
                            } else {
                                //context!!.toast(getString(androidx.camera.core.R.string.impossible_write_external_memory))
                                null
                            }
                        }

//                    imageView.imageURI = imageUrl
//                    imageView.visibility = View.VISIBLE
                        // textureView.visibility = View.GONE

                        //puisque l'image a été traiter et déplacer, on le suppprime
                        if (file.exists()) file.delete()
                        //navigateToImagePreview(imageUrl.toString())

                    }

                    override fun onError(
                        useCaseError: ImageCapture.UseCaseError,
                        message: String,
                        cause: Throwable?
                    ) {

                        requireContext().toast("Photo capture failed at ${message}")
                        cause?.printStackTrace()
                    }
                })
            }
            //====================== Image CAPTURE Config code End==========================
            CameraX.bindToLifecycle(this, preview, imageCapture) // For Preview and image Capture

            preview.enableTorch(false)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            return true
        }
        return false
    }

    private fun rotateImg(decodeBitmap: Bitmap?): Bitmap? {
        val w = decodeBitmap!!.width
        val h = decodeBitmap.height

        val matrix = Matrix()
        matrix.setRotate(90.0f)

        return Bitmap.createBitmap(decodeBitmap, 0, 0, w, h, matrix, true)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ALL_PERMISSION_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    ShowPreviewCamera()
                }
            }
        }
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = textureView.width / 5f
        val centerY = textureView.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegree = when (textureView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegree.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        textureView.setTransform(matrix)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            navBar = requireActivity().findViewById(R.id.nav_view)
            navBar.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}