package com.example.gaugeapp.utils.permissionsutils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.gaugeapp.R
import com.example.gaugeapp.utils.EntitiesUtils.PermissionResult
import com.example.gaugeapp.utils.completeDescription
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.row_item_single_permission.view.*


class ItemPermission(
    var permissionResult: PermissionResult,
    val activity: Activity,
    val viewLifecycleOwner: LifecycleOwner
) : Item() {
    companion object {
        var permissionState = MutableLiveData<Boolean>().apply {
            this.value = false
        }
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        permissionResult.completeDescription(viewHolder.itemView.context)
        Log.e("permissions",permissionResult.toString())
        initView(viewHolder)
    }

    private fun initView(viewHolder: ViewHolder) {
        viewHolder.itemView.permission_title.setText(permissionResult.name)
        viewHolder.itemView.permission_description.setText(permissionResult.descrition)
        viewHolder.itemView.permission_error_message.setText(permissionResult.limitations)
        permissionResult.image?.let { image ->
            viewHolder.itemView.permission_icon.setImageResource(image)
        }
        viewHolder.itemView.permission_btn_statu_no_cheked.setOnClickListener {

        }
    }

     private fun displayLimitationsAndCrox(viewHolder: ViewHolder){
         viewHolder.itemView.permission_btn_statu_no_cheked.visibility=View.VISIBLE
         viewHolder.itemView.permission_error_message.visibility=View.VISIBLE
     }

    override fun getLayout() = R.layout.row_item_single_permission

    private fun requestPermissions(context: Context) {
        ActivityCompat.requestPermissions(activity, arrayOf(permissionResult.permissionName), 121)
    }

}