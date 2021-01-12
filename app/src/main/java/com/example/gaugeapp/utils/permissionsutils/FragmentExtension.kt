package com.example.gaugeapp.utils.permissionsutils

import android.util.Log
import androidx.lifecycle.Observer

fun FragmentPermissions.askAnyPermission(permissions: List<String>, action: () -> Unit) {
    if (permissionResults.value == null) {
        permissionResults.value = mutableMapOf()
    }
    this.requestPermission(permissions)
    globalState.observe(viewLifecycleOwner, Observer { globalState ->
        if (globalState) {
            var i = 0
            var status = true
            permissions.forEach {
                if (permissionResults.value?.get(it)?.state == false) {
                    status = false
                }
                i++
            }

            if (status && (i == permissions.size)) {
                Log.e("status", " the status is $status")
                action()
                this.globalState.removeObservers(viewLifecycleOwner)
            }

        }
    })
}

fun FragmentPermissions.askSinglePermission(permission: String, action: () -> Unit) {
    if (permissionResults.value == null) {
        permissionResults.value = mutableMapOf()
    }
    this.requestPermission(listOf(permission))
    globalState.observe(viewLifecycleOwner, Observer { globalState ->
        if (globalState) {
            permissionResults.value?.get(permission)?.let { permissionResult ->
                permissionResult.state?.let { state ->
                    if (state) {
                        action()
                        this.globalState.removeObservers(viewLifecycleOwner)
                    }
                }

            }

        }
    })
}