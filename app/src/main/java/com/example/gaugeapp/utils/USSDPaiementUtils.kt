package com.example.gaugeapp.utils

import android.content.Context
import android.util.Log
import com.example.ussdlibrary.cons.USSDConst
import com.example.ussdlibrary.entities.Action
import com.example.ussdlibrary.entities.USSDClass
import com.example.ussdlibrary.enums.ENUM_MTN_ACTIONS
import com.example.ussdlibrary.enums.ENUM_ORANGE_ACTIONS
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.utils.PhoneNumberUtils.remove237ToPhoneNumber
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import java.lang.NullPointerException

object USSDPaiementUtils {
    val currentuserPhooneNumber = FireStoreAuthUtil.getUserPhoneNumber()

    fun processPaiement(
        context: Context,
        amount: String,
        recipientPhoneNumber: String,
        reason: String,
        pin:String,
        serviceType: ENUM_SERVICE_ANALYSIS
    ): USSDClass.USSDClassBuild? {
        var action = Action()
        when (serviceType) {
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE -> {
                // si les deux ont des numeros orange
                if (PhoneNumberUtils.isOrangeOperator(
                        PhoneNumberUtils.formatPhoneNumber(
                            recipientPhoneNumber
                        )
                    )
                ) {
                    action = ENUM_ORANGE_ACTIONS.TRANSFER_TO_ORANGE_ACCOUNT.action
                }
                //interoperabilite ORANGE - MTN
                else if (PhoneNumberUtils.isMTNOoperator(
                        PhoneNumberUtils.formatPhoneNumber(
                            recipientPhoneNumber
                        )
                    )
                ) {
                    action = ENUM_ORANGE_ACTIONS.TRANSFER_TO_MTN_ACCOUNT.action
                }
            }
            ENUM_SERVICE_ANALYSIS.TRANSFERT_MTN -> {
                // si les deux ont des numeros orange
                if (PhoneNumberUtils.isMTNOoperator(
                        PhoneNumberUtils.formatPhoneNumber(
                            recipientPhoneNumber
                        )
                    )
                ) {
                    try {
                        action = ENUM_MTN_ACTIONS.TRANSFER_TO_MTN_ACCOUNT.action.addStep(
                            USSDConst.KEY_PIN,
                            pin
                        ).addStep(USSDConst.KEY_REFERENCE, reason)
                    }catch (e: NullPointerException){
                        e.printStackTrace()
                        action = ENUM_MTN_ACTIONS.TRANSFER_TO_MTN_ACCOUNT.action.addStep(
                            USSDConst.KEY_PIN,
                            pin
                        )
                    }

                }
                //interoperabilite MTN - ORANGE
                else if (PhoneNumberUtils.isOrangeOperator(
                        PhoneNumberUtils.formatPhoneNumber(
                            recipientPhoneNumber
                        )
                    )
                ) {
                    action = ENUM_MTN_ACTIONS.TRANSFER_TO_ORANGE_ACCOUNT.action.addStep(USSDConst.KEY_REFERENCE, reason)
                }
            }
        }

        if (action != Action()) {

            val finalAction = action.addStep(USSDConst.KEY_PHONE_NUMBER, remove237ToPhoneNumber(recipientPhoneNumber))
                .addStep(USSDConst.KEY_AMOUNT, amount)
            Log.d("AMOUNT_USSD", "amount : $amount")
            return USSDClass.Builder(context).setAction(finalAction).launchUSSD()
        }else{
            return null
        }
    }


    fun buyAirtime(
        context: Context,
        action : Action
    ) = USSDClass.Builder(context).setAction(action).launchUSSD()



}