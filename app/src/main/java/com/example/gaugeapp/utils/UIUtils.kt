package com.example.gaugeapp.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import com.example.gaugeapp.R
import kotlinx.android.synthetic.main.permission_ask_accessibility_step_1.view.*
import kotlinx.android.synthetic.main.permission_ask_accessibility_step_2.view.*
import kotlinx.android.synthetic.main.permission_ask_overlay.view.*

fun simpleAlert(view: View): AlertDialog {


    view.apply {
        val dialog = AlertDialog.Builder(context)
            .setView(this)
            .create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setCancelable(true)
        dialog.show()
        return dialog
    }

}

fun openAccessibility(layoutInflater: LayoutInflater, context: Context,action: () -> Unit) {
    if (!PermissionChecked.isAccessibilitySettingsOn(context)) {
        var view = layoutInflater.inflate(R.layout.permission_ask_accessibility_step_1, null)
        val alert = simpleAlert(view)
        view.apply {
            this.permission_accessibility_btn_continue.setOnClickListener {
                alert.dismiss()
                var view =
                    layoutInflater.inflate(R.layout.permission_ask_accessibility_step_2, null)
                val alert = simpleAlert(view)
                view.apply {
                    this.permission_accessibility_step2_btn_proceed.setOnClickListener {
                        alert.dismiss()
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }else{
        action()
    }


}

fun openOverLay(layoutInflater: LayoutInflater, context: Context, action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
        var view = layoutInflater.inflate(R.layout.permission_ask_overlay, null)
        val alert = simpleAlert(view)
        view.apply {
            this.permission_overlay_step2_btn_proceed.setOnClickListener {
                alert.dismiss()
                //Android M Or Over
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName())
                )
                context.startActivity(intent)
            }
        }
    } else {
        action()
    }
}