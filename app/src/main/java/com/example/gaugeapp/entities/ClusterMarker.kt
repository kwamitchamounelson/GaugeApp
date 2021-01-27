package com.example.gaugeapp.entities

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class ClusterMarker(
    val _position: LatLng?,
    val _title: String,
    val _snippet: String,
    val iconPicture: Bitmap?,
    val store: Store?
) : ClusterItem {

    constructor() : this(null, "", "", null, null)

    /**
     * The position of this marker. This must always return the same value.
     */
    override fun getPosition(): LatLng {
        return _position!!
    }

    /**
     * The title of this marker.
     */
    override fun getTitle(): String? {
        return _title
    }

    /**
     * The description of this marker.
     */
    override fun getSnippet(): String? {
        return _snippet
    }

}
