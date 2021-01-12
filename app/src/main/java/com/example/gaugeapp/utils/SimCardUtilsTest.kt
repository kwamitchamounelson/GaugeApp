package com.example.gaugeapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import com.kola.kola_entities_features.entities.KUser
import com.kola.kola_entities_features.entities.NumberForOperator
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.enums.ENUMOPERATEUR

object SimCardUtilsTest {

    private val TAG = "SimCardUtilsTest"

    @SuppressLint("MissingPermission")
    fun getAndSaveSImCArdInfos(
        appContext: Context,
        curentUser: KUser
    ) {

        //0n recupère le numéro de téléphone avec lequel l'utilisateur s'est authentifier
        val authPhone = FireStoreAuthUtil.getUserPhoneNumber()
        Log.d(TAG, "authPhone number: $authPhone")

        val userMomoNumbFOprtor =
            getOperatorFromList(
                curentUser.mobileMoneyNumbers,
                ENUMOPERATEUR.MTN_MONEY.name
            )
        Log.d(TAG, "userMomoNumbFOprtor: $userMomoNumbFOprtor")


        val userOmNumbFOprtor =
            getOperatorFromList(
                curentUser.mobileMoneyNumbers,
                ENUMOPERATEUR.ORANGE_MONEY.name
            )
        Log.d(TAG, "userOmNumbFOprtor: $userOmNumbFOprtor")


        /**** above 22 **/
        if (Build.VERSION.SDK_INT > 22) {
            /****  for dual sim mobile  ***/
            val localSubscriptionManager =
                appContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager  //SubscriptionManager.from(appContext)

            if (localSubscriptionManager.activeSubscriptionInfoCount > 1) {

                Log.d(TAG, "The user have two SIM cards")

                /*** if there are two sim in dual sim mobile  **/
                val localList = localSubscriptionManager.activeSubscriptionInfoList

                val simInfo1 = localList[0] as SubscriptionInfo
                Log.d(TAG, "\n simInfo1: $simInfo1")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d(TAG, "\n simInfo1 cardId: ${simInfo1.cardId}")
                    Log.d(TAG, "\n simInfo1 carried id: ${simInfo1.carrierId}")
                    Log.d(TAG, "\n simInfo1 mncString: ${simInfo1.mncString}")

                }
                Log.d(TAG, "\n simInfo1 iccId: ${simInfo1.iccId}")
                Log.d(TAG, "\n simInfo1 number: ${simInfo1.number}")
                Log.d(TAG, "\n simInfo1 mcc: ${simInfo1.mcc}")
                Log.d(TAG, "\n simInfo1 mnc: ${simInfo1.mnc}")

                val simInfo2 = localList[1] as SubscriptionInfo
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d(TAG, "\n simInfo2: ${simInfo2.cardId}")
                    Log.d(TAG, "\n simInfo2 carried id: ${simInfo2.carrierId}")
                    Log.d(TAG, "\n simInfo2 mccString: ${simInfo2.mncString}")

                }
                Log.d(TAG, "\n simInfo2 iccId: ${simInfo2.iccId}")
                Log.d(TAG, "\n simInfo2 number: ${simInfo2.number}")
                Log.d(TAG, "\n simInfo2 mnc: ${simInfo2.mnc}")
                Log.d(TAG, "\n simInfo2 mnc: ${simInfo2.mnc}")

                showDeviceInformationWithTelephonyManager(appContext)


                val sim1OpName = simInfo1.carrierName.toString()
                Log.d(TAG, "\n sim1OpName: $sim1OpName")

                val sim2OpName = simInfo2.carrierName.toString()
                Log.d(TAG, "\n sim2OpName: $sim2OpName")


                if (sim1OpName == ORANGE_OPERATOR_NAME) {

                    Log.d(TAG, "\n Sim card avalaible, operator name: $sim1OpName")
                    KolaWhalletApplication.userPref.addDetectedSimCards(
                        ORANGE_OPERATOR_NAME
                    )
                }
                if (sim1OpName == MTN_OPERATOR_NAME) {

                    Log.d(TAG, "\n Sim card avalaible, operator name: $sim1OpName")
                    KolaWhalletApplication.userPref.addDetectedSimCards(
                        MTN_OPERATOR_NAME
                    )
                }

                if (sim2OpName == ORANGE_OPERATOR_NAME) {

                    Log.d(TAG, "\n Sim card avalaible, operator name: $sim2OpName")
                    KolaWhalletApplication.userPref.addDetectedSimCards(
                        ORANGE_OPERATOR_NAME
                    )
                }

                if (sim2OpName == MTN_OPERATOR_NAME) {

                    Log.d(TAG, "\n Sim card avalaible, operator name: $sim2OpName")
                    KolaWhalletApplication.userPref.addDetectedSimCards(
                        MTN_OPERATOR_NAME
                    )
                }


                // en fonction de l'opérateur on recupère l'ICCID et on l'acfte au numéro de téléphone de l'utilisateur
                when {
                    PhoneNumberUtils.isOrangeOperator(authPhone) -> {

                        Log.d(TAG, "\n Auth phone number is orange: $authPhone")
                        if (sim1OpName == ORANGE_OPERATOR_NAME) {

                            userOmNumbFOprtor?.simCartIccId = simInfo1.iccId
                            userMomoNumbFOprtor?.simCartIccId = simInfo2.iccId

                            Log.d(TAG, "\n sim1OpName is orange")
                            Log.d(TAG, "\n orange ICCID: ${simInfo1.iccId}")
                            Log.d(TAG, "\n second SIM ICCID: ${simInfo2.iccId}")


                        } else if (sim2OpName == ORANGE_OPERATOR_NAME) {
                            userOmNumbFOprtor?.simCartIccId = simInfo2.iccId
                            userMomoNumbFOprtor?.simCartIccId = simInfo1.iccId

                            Log.d(TAG, "\n sim2OpName is orange")
                            Log.d(TAG, "\n orange ICCID: ${simInfo2.iccId}")
                            Log.d(TAG, "\n second SIM ICCID: ${simInfo1.iccId}")

                        }
                    }

                    PhoneNumberUtils.isMTNOoperator(authPhone) -> {

                        Log.d(TAG, "\n Auth phone number is MTN: $authPhone")
                        if (sim1OpName == MTN_OPERATOR_NAME) {
                            userMomoNumbFOprtor?.simCartIccId = simInfo1.iccId
                            userOmNumbFOprtor?.simCartIccId = simInfo2.iccId

                            Log.d(TAG, "\n sim1OpName is MTN")
                            Log.d(TAG, "\n MTN ICCID: ${simInfo1.iccId}")
                            Log.d(TAG, "\n second SIM ICCID: ${simInfo2.iccId}")

                        } else if (sim2OpName == MTN_OPERATOR_NAME) {
                            userMomoNumbFOprtor?.simCartIccId = simInfo2.iccId
                            userOmNumbFOprtor?.simCartIccId = simInfo1.iccId

                            Log.d(TAG, "\n sim2OpName is MTN")
                            Log.d(TAG, "\n MTN ICCID: ${simInfo2.iccId}")
                            Log.d(TAG, "\n second SIM ICCID: ${simInfo1.iccId}")
                        }

                    }
                }


            } else {

                Log.d(TAG, "The user have 1 sim in dual sim mobile")

                /** if there is 1 sim in dual sim mobile **/
                val tManager =
                    appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

                val sim1OpName = tManager.networkOperatorName
                Log.d(TAG, "\n sim1OpName: $sim1OpName")

                if (sim1OpName == ORANGE_OPERATOR_NAME) {

                    Log.d(TAG, "\n Sim card avalaible, operator name: $sim1OpName")
                    KolaWhalletApplication.userPref.addDetectedSimCards(
                        ORANGE_OPERATOR_NAME
                    )
                }

                if (sim1OpName == MTN_OPERATOR_NAME) {
                    Log.d(TAG, "\n Sim card avalaible, operator name: $sim1OpName")
                    KolaWhalletApplication.userPref.addDetectedSimCards(
                        MTN_OPERATOR_NAME
                    )
                }

                // en fonction de l'opérateur on recupère l'ICCID et on l'acfte au numéro de téléphone de l'utilisateur
                when {
                    PhoneNumberUtils.isOrangeOperator(authPhone) -> {

                        Log.d(TAG, "\n Auth phone number is orange: $authPhone")

                        if (sim1OpName == ORANGE_OPERATOR_NAME) {
                            userOmNumbFOprtor?.simCartIccId = tManager.simSerialNumber

                            Log.d(TAG, "\n sim1OpName is orange")
                            Log.d(TAG, "\n orange ICCID: ${userOmNumbFOprtor?.simCartIccId}")
                        }
                    }

                    PhoneNumberUtils.isMTNOoperator(authPhone) -> {

                        if (sim1OpName == MTN_OPERATOR_NAME) {
                            userMomoNumbFOprtor?.simCartIccId = tManager.simSerialNumber

                            Log.d(TAG, "\n sim1OpName is Mtn")
                            Log.d(TAG, "\n Mtn ICCID: ${userMomoNumbFOprtor?.simCartIccId}")
                        }

                    }
                }
            }
        } else {

            /*** below android version 22 ***/
            val tManager =
                appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val sim1OpName = tManager.networkOperatorName

            showDeviceInformationWithTelephonyManager(appContext)

            if (sim1OpName == ORANGE_OPERATOR_NAME) {
                KolaWhalletApplication.userPref.addDetectedSimCards(
                    ORANGE_OPERATOR_NAME
                )
            }
            if (sim1OpName == MTN_OPERATOR_NAME) {
                KolaWhalletApplication.userPref.addDetectedSimCards(
                    MTN_OPERATOR_NAME
                )
            }

            // en fonction de l'opérateur on recupère l'ICCID et on l'afecte au numéro de téléphone de l'utilisateur
            when {
                PhoneNumberUtils.isOrangeOperator(authPhone) -> {

                    if (sim1OpName == ORANGE_OPERATOR_NAME) {
                        userOmNumbFOprtor?.simCartIccId = tManager.simSerialNumber
                    }

                }
                PhoneNumberUtils.isMTNOoperator(authPhone) -> {

                    if (sim1OpName == MTN_OPERATOR_NAME) {
                        userMomoNumbFOprtor?.simCartIccId = tManager.simSerialNumber
                    }

                }
            }
        }

        // update userInformations
        val opList = ArrayList<NumberForOperator>()
        userOmNumbFOprtor?.let { opList.add(it) }
        userMomoNumbFOprtor?.let { opList.add(it) }

        Log.d(TAG, "OpList information:")
        opList.forEach {
            Log.d(TAG, "\n $it")
        }
    }

    @SuppressLint("MissingPermission")
    private fun showDeviceInformationWithTelephonyManager(appContext: Context){
        val tManager =
            appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val sim1OpName = tManager.networkOperatorName

        Log.d(TAG, "\n ********************************** infor with telephonyManager**************************************")

        Log.d(TAG, "\n simInfo1 simSerialNumber (iccId): ${tManager.simSerialNumber}")
        Log.d(TAG, "\n simInfo1 number: ${tManager.line1Number}")
        Log.d(TAG, "\n simInfo1 mcc: ${tManager.networkOperator.substring(0,3)}")
        Log.d(TAG, "\n simInfo1 mnc: ${tManager.networkOperator.substring(3)}")
        Log.d(TAG, "\n simInfo1 imei: ${tManager.deviceId}")

        Log.d(TAG, "\n simInfo1 network operator name $sim1OpName")
        Log.d(TAG, "\n simInfo1 Sim operator name: ${tManager.simOperatorName}")

        Log.d(TAG, "\n ********************************** infor with telephonyManage ENDr**************************************")

    }
}