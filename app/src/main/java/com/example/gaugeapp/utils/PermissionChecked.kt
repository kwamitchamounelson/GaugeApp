package com.example.gaugeapp.utils

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import com.example.ussdlibrary.services.USSDService

object PermissionChecked {

    val TAG = "PermissionCkecked"

    fun isAccessibilitySettingsOn(mContext : Context) : Boolean {
        var accessibilityEnabled = 0
        val service = mContext.getPackageName() + "/" + USSDService::class.java.getCanonicalName()
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.getApplicationContext().getContentResolver(),
                android.provider.Settings.Secure.ACCESSIBILITY_ENABLED)
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled)
        } catch (e : Settings.SettingNotFoundException ) {
            Log.e(
                TAG, "Error finding setting, default accessibility to not found: "
                    + e.message)
        }
        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            val settingValue = Settings.Secure.getString(
                mContext.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service)
                    if (accessibilityService.equals(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!")
                        return true
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***")
        }

        return false
    }

}