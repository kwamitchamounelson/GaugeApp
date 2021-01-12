package com.example.gaugeapp.utils.EntitiesUtils

import android.graphics.Bitmap
import android.net.Uri

data class CustumImageClass(val bitmap: Bitmap?, val imageLocation: Uri?) {
    constructor():this(null,null)
}