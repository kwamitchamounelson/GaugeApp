package com.example.gaugeapp.utils.map

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.example.gaugeapp.R
import com.example.gaugeapp.entities.ClusterMarker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MyClusterManagerRenderer(
    val context: Context?,
    val map: GoogleMap?,
    clusterManager: ClusterManager<ClusterMarker>?
) : DefaultClusterRenderer<ClusterMarker>(context, map, clusterManager) {

    constructor() : this(null, null, null)

    private var markerWith: Int = context!!.resources.getDimension(R.dimen.dimen_300dp).toInt()

    private var markerHeight: Int =
        context!!.resources.getDimension(R.dimen.dimen_300dp).toInt()

    private var imageView = ImageView(context?.applicationContext).apply {
        layoutParams = ViewGroup.LayoutParams(markerWith, markerHeight)
        val padding: Int = context.resources.getDimension(R.dimen.dimen_5dp).toInt()
        this.setPadding(padding, padding, padding, padding)
    }


    /*private final var iconGenerator = IconGenerator(context?.applicationContext).apply {
        setContentView(imageView)
    }*/

    /**
     * Called before the marker for a ClusterItem is added to the map. The default implementation
     * sets the marker and snippet text based on the respective item text if they are both
     * available, otherwise it will set the title if available, and if not it will set the marker
     * title to the item snippet text if that is available.
     *
     * The first time [ClusterManager.cluster] is invoked on a set of items
     * [.onBeforeClusterItemRendered] will be called and
     * [.onClusterItemUpdated] will not be called.
     * If an item is removed and re-added (or updated) and [ClusterManager.cluster] is
     * invoked again, then [.onClusterItemUpdated] will be called and
     * [.onBeforeClusterItemRendered] will not be called.
     *
     * @param item item to be rendered
     * @param markerOptions the markerOptions representing the provided item
     */
    override fun onBeforeClusterItemRendered(item: ClusterMarker, markerOptions: MarkerOptions) {
        imageView.setImageBitmap(item.iconPicture)
        markerOptions
            .icon(BitmapDescriptorFactory.fromBitmap(item.iconPicture))
            .title(item.title)
    }


    /**
     * Determine whether the cluster should be rendered as individual markers or a cluster.
     * @param cluster cluster to examine for rendering
     * @return true if the provided cluster should be rendered as a single marker on the map, false
     * if the items within this cluster should be rendered as individual markers instead.
     */
    override fun shouldRenderAsCluster(cluster: Cluster<ClusterMarker>): Boolean {
        return false
    }
}