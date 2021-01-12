package com.example.gaugeapp.utils.smsUtils

import android.annotation.SuppressLint
import android.util.Log
import com.example.gaugeapp.commonRepositories.smsRep.SMSFirestoreRepository
import com.example.gaugeapp.utils.remoteConfig.USSDRemoteConfigUtils
import com.kola.smsmodule.util.RegexConfig
import com.kola.smsmodule.util.SmsPhoneNumberUtils


object ExtractNameFromUSSDMessage {

    val TAG = "ExtractNameFromUSSDMessage"

    @SuppressLint("LongLogTag")
    private fun isTransfertMessageOrange(message: String): Boolean {

        Log.d(
            TAG,
            "***isTransfer1 orange ${USSDRemoteConfigUtils.get_IsTransfertMessageOrangeEng_Pattern()}"
        )
        Log.d(
            TAG,
            "***isTransfer2 orange ${USSDRemoteConfigUtils.get_IsTransfertMessageOrangeFra_Pattern()}"
        )

        val firstControle = message.contains(
            USSDRemoteConfigUtils.get_IsTransfertMessageOrangeEng_Pattern(),
            true
        ) || message.contains(USSDRemoteConfigUtils.get_IsTransfertMessageOrangeFra_Pattern())

        if (firstControle) {
            val phoneNumberValue = getPhoneNumberFromMessage(message)

            //c'est un message ussd de transfert orange que si le numéro de telephone contenue dans le
            // message est un numéro orange si le message ussd n'est pas un message d'inter operabilité mtn
            if (SmsPhoneNumberUtils.isOrangeOperator(phoneNumberValue)
                && !isMtnInterOperabilityUssdMessage(message)
            ) {
                return true
            }
        }
        return false
    }

    @SuppressLint("LongLogTag")
    private fun isTransfertMessageMTNEnglish(message: String): Boolean {

        Log.d(
            TAG,
            "***isTransfer MTN eng ${USSDRemoteConfigUtils.get_IsTransfertMessageMTNEng_Pattern()}"
        )

        val firstControle =
            message.contains(USSDRemoteConfigUtils.get_IsTransfertMessageMTNEng_Pattern().toRegex())

        if (firstControle) {
            //c'est un message ussd de transfert mtn que si le numéro de telephone contenue dans le
            // message est un numéro mtn si le message ussd n'est pas un message d'inter operabilité orange
            if (isTransfertMessageMtnSecondControl(message)) {
                return true
            }
        }
        return false
    }

    @SuppressLint("LongLogTag")
    private fun isTransfertMessageMTNFrench(message: String): Boolean {
        Log.d(
            TAG,
            "***isTransfer MTN french ${USSDRemoteConfigUtils.get_IsTransfertMessageMTNFra_Pattern()}"
        )
        val firstControle =
            message.contains(USSDRemoteConfigUtils.get_IsTransfertMessageMTNFra_Pattern().toRegex())
        if (firstControle) {
            //c'est un message ussd de transfert mtn que si le numéro de telephone contenue dans le
            // message est un numéro mtn si le message ussd n'est pas un message d'inter operabilité orange
            if (isTransfertMessageMtnSecondControl(message)) {
                return true
            }
        }

        return false
    }

    @SuppressLint("LongLogTag")
    fun run(message: String) {
        if (isTransfertMessageOrange(message)) {
            Log.d(TAG, "is Orange Message")
            traitmentOfOrangeMessage(message)
        }

        if (isTransfertMessageMTNFrench(message)) {
            Log.d(TAG, "is MTN french Message")
            traitmentOfMTNMessage(message, true)
        }

        if (isTransfertMessageMTNEnglish(message)) {
            Log.d(TAG, "is MTN english Message")
            traitmentOfMTNMessage(message, false)
        }
    }

    @SuppressLint("LongLogTag")
    private fun traitmentOfOrangeMessage(message: String) {
        val phoneNumber = getPhoneNumberFromMessage(message)
        val name = getNameFromOrangeUSSD(message)

        SMSFirestoreRepository.associateTransactionNameWithPhoneNumber(
            phoneNumber,
            name.trim(),
            true
        )
        SMSFirestoreRepository.saveDetectedNameFromUSSDToUser(name.trim(), phoneNumber)
    }

    private fun traitmentOfMTNMessage(message: String, isFrench: Boolean) {
        val phoneNumber = getPhoneNumberFromMessage(message)
        val name = getNameFromMtnUSSD(message, isFrench)
        Log.e("EnterPINCodeFragment", name.trim() + " " + phoneNumber)

        SMSFirestoreRepository.associateTransactionNameWithPhoneNumber(
            phoneNumber,
            name.trim(),
            true
        )
        SMSFirestoreRepository.saveDetectedNameFromUSSDToUser(name.trim(), phoneNumber)
    }

    @SuppressLint("LongLogTag")
    private fun preprocessingForUssdMessages(ussdMessage: String): String {

        val regexForSpecialCaracters =
            RegexConfig.VALUE_PREPROCESSING_SPECIAL_CARACTERS_PATTERN.toRegex() //"""[^\w \,\.]"""

        Log.d(TAG, "*****regexForSpecialCaracters = $regexForSpecialCaracters")


        var message = ussdMessage.toUpperCase()

        //on retir les caractères spéciaux
        message = message.replace(regexForSpecialCaracters, " ")
        //on retir le A, les parenthèses ouvrantes et fermentes et les FCFA

        val regexPreprocessing =
            USSDRemoteConfigUtils.get_Preprocessing_ExtractName_Pattern()//"""(\bA\b|\(|\)|FCFA)"""

        message = message.replace(regexPreprocessing.toRegex(), " ")
        return message
    }

    private fun getPhoneNumberFromMessage(message: String): String {
        val phoneNumberRegex = USSDRemoteConfigUtils.get_PhoneNumber_Pattern()
            .toRegex() //"""(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()

        val phoneNumberRegexFinder = phoneNumberRegex.find(message)

        val phoneNumberValue = phoneNumberRegexFinder?.value

        return (phoneNumberValue ?: "").trim()
    }

    @SuppressLint("LongLogTag")
    private fun getNameFromOrangeUSSD(ussdMessage: String): String {

        val message = ussdMessage.toUpperCase()

        val phoneNumberRegex = USSDRemoteConfigUtils.get_PhoneNumber_Pattern()
            .toRegex() //"""(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()
        val feesRegex = USSDRemoteConfigUtils.get_GetName_Fees_Pattern()
            .toRegex() //"""\.?\s*(FRAIS|FEES)""".toRegex()

        Log.d(TAG, "***** orange phoneNumberRegex= $phoneNumberRegex")
        Log.d(TAG, "***** orange feesRegex= $feesRegex")


        val phoneNumberRegexFinder = phoneNumberRegex.find(message)
        val phoneNumberRangeEnd = phoneNumberRegexFinder?.range?.last

        val feesRegexFinder = feesRegex.find(message)
        val feesRangeStart = feesRegexFinder?.range?.first

        val startIndex = (phoneNumberRangeEnd ?: 0) + 1
        val endIndex = (feesRangeStart ?: 1)

        val findedName = try {
            message.subSequence(startIndex, endIndex)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }

        return findedName.toString().trim()
    }

    @SuppressLint("LongLogTag")
    private fun getNameFromMtnUSSD(ussdMessage: String, frenchLanguage: Boolean): String {
        var message = ussdMessage.toUpperCase()

        val numericalValueRegex =
            RegexConfig.VALUE_NUMERICAL_VALUES_PATTERN.toRegex()

        val phoneNumberRegex = USSDRemoteConfigUtils.get_PhoneNumber_Pattern()
            .toRegex() //"""(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()

        Log.d(TAG, "*****MTN numericalValueRegex= $numericalValueRegex")
        Log.d(TAG, "*****MTN phoneNumberRegex= $numericalValueRegex")


        //French language
//        if (frenchLanguage) {

            //on retir le A et les parenthèses
            message = preprocessingForUssdMessages(message)

            val numericalValueFinder = numericalValueRegex.find(message)
            val phoneNumberRegexFinder = phoneNumberRegex.find(message)

            val numericalValueRangeEnd = numericalValueFinder?.range?.last
            val phoneNumberRangeStart = phoneNumberRegexFinder?.range?.first

            val startIndex = if(!frenchLanguage)(numericalValueRangeEnd ?: 0) + 7 else (numericalValueRangeEnd ?: 0) + 3
            val endIndex = (phoneNumberRangeStart ?: 1)

            val findedName = try {
                message.subSequence(startIndex, endIndex)
            } catch (ex: Exception) {
                ex.printStackTrace()
                ""
            }

            return findedName.toString().trim()

//        }

        //english language
//        if (!frenchLanguage) {
//
//            val endNameRegex = USSDRemoteConfigUtils.get_GetNameMtnEng_End_Pattern()
//                .toRegex() //"""\s*1\)""".toRegex()
//            Log.d(TAG, "*****MTN endNameRegex= $endNameRegex")
//
//            val phoneNumberValueFinder = phoneNumberRegex.find(message)
//            val endNameValueFinder = endNameRegex.find(message)
//
//            val phoneNumberRangeEnd = phoneNumberValueFinder?.range?.last
//            val endNameRangeStart = endNameValueFinder?.range?.first
//
//            val startIndex = (phoneNumberRangeEnd ?: 0) + 1
//            val endIndex = (endNameRangeStart ?: 1)
//
//            val findedName = try {
//                message.subSequence(startIndex, endIndex)
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//                ""
//            }
//            return findedName.toString().trim()
//        }

//        return ""
    }


    fun isOrangeInterOperabilityUssdMessage(ussdMessage: String): Boolean {

        val upperCaseMessage = ussdMessage.toUpperCase()

        val phoneNumberRegex = """(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()
        val phoneNumberRegexFinder = phoneNumberRegex.find(upperCaseMessage)

        var phoneNumberValue = phoneNumberRegexFinder?.value
        phoneNumberValue = phoneNumberValue?.trim()

        println("Phone number value: $phoneNumberValue")

        // c'est une inter operabilité orange que si le message contient un mot clé MTN et un numéro MTN
        if (upperCaseMessage.contains("TRANSFER")
            && upperCaseMessage.contains("MTN CAMEROON")
            && SmsPhoneNumberUtils.isMTNOperator(phoneNumberValue ?: "")
        ) {
            return true
        }

        return false
    }

    fun isMtnInterOperabilityUssdMessage(ussdMessage: String): Boolean {

        val upperCaseMessage = ussdMessage.toUpperCase()

        val phoneNumberValue = getPhoneNumberFromMessage(upperCaseMessage)

        println("Phone number value: $phoneNumberValue")
        // c'est une inter operabilité MTN que si le message contient un mot clé CODE PIN et un numéro ORANGE
        if (upperCaseMessage.contains("CODE PIN POUR CONFIRMER LE TRANSFER")
            && SmsPhoneNumberUtils.isOrangeOperator(phoneNumberValue ?: "")
        ) {
            return true
        }

        return false
    }

    private fun isTransfertMessageMtnSecondControl(message: String): Boolean {

        var phoneNumberValue = getPhoneNumberFromMessage(message)

        //c'est un message ussd de transfert mtn que si le numéro de telephone contenue dans le
        // message est un numéro mtn si le message ussd n'est pas un message d'inter operabilité orange
        if (SmsPhoneNumberUtils.isMTNOperator(phoneNumberValue ?: "")
            && !isOrangeInterOperabilityUssdMessage(message)
        ) {
            return true
        }

        return false
    }


}