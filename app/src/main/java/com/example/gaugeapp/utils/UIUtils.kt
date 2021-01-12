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