package com.example.gaugeapp.utils.remoteConfig

import android.annotation.SuppressLint
import android.util.Log
import com.example.gaugeapp.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.example.gaugeapp.data.entities.Release

object AppReleaseRemoteConfigUtils {

    val TAG = "AppReleaseRemoteConfigUtils"

    const val KEY_CHAT_NOTIFICATIONS_MESSAGE = "CHAT_NOTIFICATIONS_MESSAGES"
    const val KEY_CHAT_NEW_NOTIFICATIONS_MSS_IS_THERE = "KEY_CHAT_NEW_NOTIFICATIONS_MSS_IS_THERE"

    const val KEY_RESLEASE_SENZITIVITY = "UPDATE_SENZITIVITY"
    const val KEY_LATEST_VERSION_NAME = "LATEST_VERSION_NAME"
    const val KEY_LATEST_VERSION_CODE = "LATEST_VERSION_CODE"
    const val KEY_LATEST_RELEASE_URL = "LATEST_RELEASE_URL"
    const val KEY_IS_MUST_LAUNCH_USSD_STEP_BY_STEP = "IS_MUST_LAUNCH_USSD_STEP_BY_STEP"
    const val KEY_IS_IGNORED_USERS_ANALYSED_SMSs = "IS_IGNORED_USERS_ANALYSED_SMSs"
    const val KEY_REANALYZED_REPORTED_TRANSACTION_VERSION_CODE =
        "REANALYZED_REPORTED_TRANSACTION_VERSION_CODE"

    const val KEY_SHARE_DESCRIPTION = "SHARE_DESCRIPTION"
    const val KEY_ILLUSTRATION_URL = "ILLUSTRATION_URL"

    private val remoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    @SuppressLint("LongLogTag")
    fun fetchRemoteConfigFromServer(onComplete: () -> Unit) {


        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)


        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    Log.d(TAG, "Remote config Fetch and activate succeeded")
                    remoteConfig.activate()
                    onComplete()
                } else {
                    Log.d(TAG, "Remote config Fetch failed")
                    onComplete()
                }
            }
    }

    fun getLatesNotifiationMessage(keyOfNotification: String): String {
        return remoteConfig.getString(keyOfNotification)
    }

    fun getisNewNotifiationMessageIsThere(keyOfNotificationIsThere: String): Boolean {
        return remoteConfig.getBoolean(keyOfNotificationIsThere)
    }

    fun getCurrentRelease(): Release {
        val versionName =
            getLatestVersionName()
        val updateSenzibility =
            getUpdateSenzitivity()
        val latestReleaseUrl =
            getLatestReleaseUrl()

        val release = Release(latestReleaseUrl, versionName, updateSenzibility)
        release.shareDescription = getShareDescription()
        release.illustrationUrl = getIllustrationUrl()
        release.versionCode = getLatestVersionCode()
        return release
    }

    private fun getLatestVersionName(): String {
        return remoteConfig.getString(
            KEY_LATEST_VERSION_NAME
        )
    }

    private fun getLatestVersionCode(): Long {
        return remoteConfig.getLong(
            KEY_LATEST_VERSION_CODE
        )
    }

    private fun getUpdateSenzitivity(): Boolean {
        return remoteConfig.getBoolean(
            KEY_RESLEASE_SENZITIVITY
        )
    }

    private fun getLatestReleaseUrl(): String {
        return remoteConfig.getString(
            KEY_LATEST_RELEASE_URL
        )
    }

    private fun getShareDescription(): String {
        return remoteConfig.getString(KEY_SHARE_DESCRIPTION)
    }

    private fun getIllustrationUrl(): String {
        return remoteConfig.getString(KEY_ILLUSTRATION_URL)
    }

    fun getReanalyseReportedTransactionCode(): Long {
        return remoteConfig.getLong(KEY_REANALYZED_REPORTED_TRANSACTION_VERSION_CODE)
    }

    fun getUSSDMustLaunchStepByStep(): Boolean {
        return remoteConfig.getBoolean(
            KEY_IS_MUST_LAUNCH_USSD_STEP_BY_STEP
        )
    }

    fun isIgnoreUserAnalysedSmss(): Boolean {
        return remoteConfig.getBoolean(
            KEY_IS_IGNORED_USERS_ANALYSED_SMSs
        )
    }
}