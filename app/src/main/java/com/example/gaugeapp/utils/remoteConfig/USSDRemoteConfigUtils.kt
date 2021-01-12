package com.example.gaugeapp.utils.remoteConfig

import android.annotation.SuppressLint
import android.util.Log
import com.example.gaugeapp.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object USSDRemoteConfigUtils {

    private const val KEY_GET_NAME_MTN_ENG_END_PATTERN = "GET_NAME_MTN_ENG_END_PATTERN"
    private const val KEY_GET_NAME_FEES_PATTERN = "GET_NAME_FEES_PATTERN"
    private const val KEY_PHONE_NUMBER_REGEX = "PHONE_NUMBER_REGEX"
    private const val KEY_PREPROCESSING_EXTRACT_NAME_PATTERN = "PREPROCESSING_EXTRACT_NAME_PATTERN"
    private const val KEY_IS_TRANSFERT_MESSAGE_MTN_ENG_PATTERN = "IS_TRANSFERT_MESSAGE_MTN_ENG_PATTERN"
    private const val KEY_IS_TRANSFERT_MESSAGE_MTN_FRA_PATTERN = "IS_TRANSFERT_MESSAGE_MTN_FRA_PATTERN"
    private const val KEY_IS_TRANSFERT_MESSAGE_ORANGE_FRA_PATTERN = "IS_TRANSFERT_MESSAGE_ORANGE_FRA_PATTERN"
    private const val KEY_IS_TRANSFERT_MESSAGE_ORANGE_ENG_PATTERN = "IS_TRANSFERT_MESSAGE_ORANGE_ENG_PATTERN"

    private val remoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    @SuppressLint("LongLogTag")
    fun fetchRemoteUSSDConfigFromServer() {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_ussd_config_default)


        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    Log.d(SMSRemoteConfigUtils.TAG, "Remote config Fetch and activate succeeded")
                    remoteConfig.activate()
                } else {
                    Log.d(SMSRemoteConfigUtils.TAG, "Remote config Fetch failed")
                }
            }
    }

    fun get_GetNameMtnEng_End_Pattern () : String{
        return remoteConfig.getString(KEY_GET_NAME_MTN_ENG_END_PATTERN)
    }

    fun get_GetName_Fees_Pattern() : String{
        return remoteConfig.getString(KEY_GET_NAME_FEES_PATTERN)
    }

    fun get_PhoneNumber_Pattern () : String{
        return remoteConfig.getString(KEY_PHONE_NUMBER_REGEX)
    }

    fun get_Preprocessing_ExtractName_Pattern() : String{
        return remoteConfig.getString(KEY_PREPROCESSING_EXTRACT_NAME_PATTERN)
    }

    fun get_IsTransfertMessageOrangeEng_Pattern() : String{
        return remoteConfig.getString(KEY_IS_TRANSFERT_MESSAGE_ORANGE_ENG_PATTERN)
    }

    fun get_IsTransfertMessageOrangeFra_Pattern() : String{
        return remoteConfig.getString(KEY_IS_TRANSFERT_MESSAGE_ORANGE_FRA_PATTERN)
    }

    fun get_IsTransfertMessageMTNEng_Pattern() : String{
        return remoteConfig.getString(KEY_IS_TRANSFERT_MESSAGE_MTN_ENG_PATTERN)
    }

    fun get_IsTransfertMessageMTNFra_Pattern() : String{
        return remoteConfig.getString(KEY_IS_TRANSFERT_MESSAGE_MTN_FRA_PATTERN)
    }

}