package com.example.gaugeapp.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.example.gaugeapp.R
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.example.gaugeapp.utils.EntitiesUtils.CustumImageClass
import org.jetbrains.anko.toast
import java.io.File

object GalleryUtils {
    /**
     * Getting All Images Path.
     *
     * Required Storage Permission
     *
     * @return ArrayList with images Bitmap
     */
    fun getLastesImages(context: Context, nombreDimage: Int): ArrayList<CustumImageClass> {
        val imageBitmapList = ArrayList<CustumImageClass>()
        val projection = arrayOf<String>(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        )

        var imageCount = 0
        while (cursor!!.moveToNext() && (imageCount < nombreDimage)) {
            val imageUri = Uri.fromFile(File(cursor.getString(1)))
            val path = imageUri.path
            val imageFile = File(path)
            if (imageFile.exists()) {
                try {
                    val bitmap = BitmapFactory.decodeFile(path)
                    imageBitmapList.add(CustumImageClass(bitmap, imageUri))
                }catch (e: OutOfMemoryError){
                    e.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(e)
                    context.toast(context.resources.getString(R.string.error_message))
                }
            }
            imageCount++
        }
        return imageBitmapList
    }

}