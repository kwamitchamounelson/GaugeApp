package com.example.gaugeapp.utils.remoteConfig

import android.annotation.SuppressLint
import android.util.Log
import com.example.gaugeapp.R
import com.example.ussdlibrary.cons.remoteStepConst
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object USSDStepsRemoteConfigUtils {
    val TAG = "USSDStepsRemoteConfigUtils"

    private const val KEY_ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_1 = "ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_1"
    private const val KEY_ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_2 = "ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_2"
    private const val KEY_ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_3 = "ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_3"
    private const val KEY_ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_1 = "ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_1"
    private const val KEY_ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_2 = "ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_2"
    private const val KEY_ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_3 = "ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_3"
    private const val KEY_ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1 = "ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1"
    private const val KEY_ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2 = "ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2"
    private const val KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_1 = "ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_1"
    private const val KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_2 = "ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_2"
    private const val KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_3 = "ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_3"
    private const val KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_4 = "ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_4"
    private const val KEY_MTN_AIRTIME_IN_MY_ACCOUNT_STEP_1 = "MTN_AIRTIME_IN_MY_ACCOUNT_STEP_1"
    private const val KEY_MTN_AIRTIME_IN_MY_ACCOUNT_STEP_2 = "MTN_AIRTIME_IN_MY_ACCOUNT_STEP_2"
    private const val KEY_MTN_AIRTIME_IN_MY_ACCOUNT_STEP_3 = "MTN_AIRTIME_IN_MY_ACCOUNT_STEP_3"
    private const val KEY_MTN_AIRTIME_TO_MTN_NUMBER_STEP_1 = "MTN_AIRTIME_TO_MTN_NUMBER_STEP_1"
    private const val KEY_MTN_AIRTIME_TO_MTN_NUMBER_STEP_2 = "MTN_AIRTIME_TO_MTN_NUMBER_STEP_2"
    private const val KEY_MTN_AIRTIME_TO_MTN_NUMBER_STEP_3 = "MTN_AIRTIME_TO_MTN_NUMBER_STEP_3"
    private const val KEY_MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_1 = "MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_1"
    private const val KEY_MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_2 = "MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_2"
    private const val KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1 = "MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1"
    private const val KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2 = "MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2"
    private const val KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_3 = "MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_3"
    private const val KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_4 = "MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_4"

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    @SuppressLint("LongLogTag")
    fun fetchRemoteConfigFromServer() {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_ussd_steps_default)


        // [START fetch_config_with_callback]
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val updated = it.result
                    Log.d(TAG, "Remote config Fetch and activate succeeded")
                    remoteConfig.activate()
                    setRegexFromRemote()
                } else {
                    Log.d(TAG, "Remote config Fetch failed")
                }
            }
    }

    fun get_Orange_AirtimeInMyAccount_Step1(): String {
        return remoteConfig.getString(
            KEY_ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_1
        )
    }

    fun get_Orange_AirtimeInMyAccount_Step2(): String {
        return remoteConfig.getString(
            KEY_ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_2
        )
    }

    fun get_Orange_AirtimeInMyAccount_Step3(): String {
        return remoteConfig.getString(
            KEY_ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_3
        )
    }

    fun get_Orange_AirtimeToOrangeNumber_Step1(): String {
        return remoteConfig.getString(
            KEY_ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_1
        )
    }

    fun get_Orange_AirtimeToOrangeNumber_Step2(): String {
        return remoteConfig.getString(
            KEY_ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_2
        )
    }

    fun get_Orange_AirtimeToOrangeNumber_Step3(): String {
        return remoteConfig.getString(
            KEY_ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_3
        )
    }

    fun get_Orange_TransferToOrangeAccount_Step1(): String {
        return remoteConfig.getString(
            KEY_ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1
        )
    }

    fun get_Orange_TransferToOrangeAccount_Step2(): String {
        return remoteConfig.getString(
            KEY_ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2
        )
    }

    fun get_Orange_TransferToMtnAccount_Step1(): String {
        return remoteConfig.getString(
            KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_1
        )
    }

    fun get_Orange_TransferToMtnAccount_Step2(): String {
        return remoteConfig.getString(
            KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_2
        )
    }

    fun get_Orange_TransferToMtnAccount_Step3(): String {
        return remoteConfig.getString(
            KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_3
        )
    }

    fun get_Orange_TransferToMtnAccount_Step4(): String {
        return remoteConfig.getString(
            KEY_ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_4
        )
    }

    fun get_Mtn_AirtimeInMyAccount_Step1(): String {
        return remoteConfig.getString(
            KEY_MTN_AIRTIME_IN_MY_ACCOUNT_STEP_1
        )
    }

    fun get_Mtn_AirtimeInMyAccount_Step2(): String {
        return remoteConfig.getString(
            KEY_MTN_AIRTIME_IN_MY_ACCOUNT_STEP_2
        )
    }

    fun get_Mtn_AirtimeInMyAccount_Step3(): String {
        return remoteConfig.getString(
            KEY_MTN_AIRTIME_IN_MY_ACCOUNT_STEP_3
        )
    }

    fun get_Mtn_AirtimeToMtnNumber_Step1(): String {
        return remoteConfig.getString(
            KEY_MTN_AIRTIME_TO_MTN_NUMBER_STEP_1
        )
    }

    fun get_Mtn_AirtimeToMtnNumber_Step2(): String {
        return remoteConfig.getString(
            KEY_MTN_AIRTIME_TO_MTN_NUMBER_STEP_2
        )
    }

    fun get_Mtn_AirtimeToMtnNumber_Step3(): String {
        return remoteConfig.getString(
            KEY_MTN_AIRTIME_TO_MTN_NUMBER_STEP_3
        )
    }

    fun get_Mtn_TransferToMtnAccount_Step1(): String {
        return remoteConfig.getString(
            KEY_MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_1
        )
    }

    fun get_Mtn_TransferToMtnAccount_Step2(): String {
        return remoteConfig.getString(
            KEY_MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_2
        )
    }

    fun get_Mtn_TransferToOrangeAccount_Step1(): String {
        return remoteConfig.getString(
            KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1
        )
    }

    fun get_Mtn_TransferToOrangeAccount_Step2(): String {
        return remoteConfig.getString(
            KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2
        )
    }

    fun get_Mtn_TransferToOrangeAccount_Step3(): String {
        return remoteConfig.getString(
            KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_3
        )
    }

    fun get_Mtn_TransferToOrangeAccount_Step4(): String {
        return remoteConfig.getString(
            KEY_MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_4
        )
    }

    fun setRegexFromRemote(){
        remoteStepConst.ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_1 = get_Orange_AirtimeInMyAccount_Step1()
        remoteStepConst.ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_2 = get_Orange_AirtimeInMyAccount_Step2()
        remoteStepConst.ORANGE_AIRTIME_IN_MY_ACCOUNT_STEP_3 = get_Orange_AirtimeInMyAccount_Step3()
        remoteStepConst.ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_1 = get_Orange_AirtimeToOrangeNumber_Step1()
        remoteStepConst.ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_2 = get_Orange_AirtimeToOrangeNumber_Step2()
        remoteStepConst.ORANGE_AIRTIME_TO_ORANGE_NUMBER_STEP_3 = get_Orange_AirtimeToOrangeNumber_Step3()
        remoteStepConst.ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1 = get_Orange_TransferToOrangeAccount_Step1()
        remoteStepConst.ORANGE_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2 = get_Orange_TransferToOrangeAccount_Step2()
        remoteStepConst.ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_1 = get_Orange_TransferToMtnAccount_Step1()
        remoteStepConst.ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_2 = get_Orange_TransferToMtnAccount_Step2()
        remoteStepConst.ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_3 = get_Orange_TransferToMtnAccount_Step3()
        remoteStepConst.ORANGE_TRANSFER_TO_MTN_ACCOUNT_STEP_4 = get_Orange_TransferToMtnAccount_Step4()

        //Mtn constantes
        remoteStepConst.MTN_AIRTIME_IN_MY_ACCOUNT_STEP_1 = get_Mtn_AirtimeInMyAccount_Step1()
        remoteStepConst.MTN_AIRTIME_IN_MY_ACCOUNT_STEP_2 = get_Mtn_AirtimeInMyAccount_Step2()
        remoteStepConst.MTN_AIRTIME_IN_MY_ACCOUNT_STEP_3 = get_Mtn_AirtimeInMyAccount_Step3()
        remoteStepConst.MTN_AIRTIME_TO_MTN_NUMBER_STEP_1 = get_Mtn_AirtimeToMtnNumber_Step1()
        remoteStepConst.MTN_AIRTIME_TO_MTN_NUMBER_STEP_2 = get_Mtn_AirtimeToMtnNumber_Step2()
        remoteStepConst.MTN_AIRTIME_TO_MTN_NUMBER_STEP_3 = get_Mtn_AirtimeToMtnNumber_Step3()
        remoteStepConst.MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_1 = get_Mtn_TransferToMtnAccount_Step1()
        remoteStepConst.MTN_TRANSFER_TO_MTN_ACCOUNT_STEP_2 = get_Mtn_TransferToMtnAccount_Step2()
        remoteStepConst.MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_1 = get_Mtn_TransferToOrangeAccount_Step1()
        remoteStepConst.MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_2 = get_Mtn_TransferToOrangeAccount_Step2()
        remoteStepConst.MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_3 = get_Mtn_TransferToOrangeAccount_Step3()
        remoteStepConst.MTN_TRANSFER_TO_ORANGE_ACCOUNT_STEP_4 = get_Mtn_TransferToOrangeAccount_Step4()

    }
}