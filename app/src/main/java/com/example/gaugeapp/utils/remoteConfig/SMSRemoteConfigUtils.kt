package com.example.gaugeapp.utils.remoteConfig

import android.annotation.SuppressLint
import android.util.Log
import com.example.gaugeapp.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kola.smsmodule.util.RegexConfig


object SMSRemoteConfigUtils {

    val TAG = "AppReleaseRemoteConfigUtils"

    private const val KEY_PREPROCESSING_ID_TRANSACTION_ORANGE_PATTERN =
        "PREPROCESSING_ID_TRANSACTION_ORANGE_PATTERN"
    private const val KEY_PREPROCESSING_DATE_PATTERN = "PREPROCESSING_DATE_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_CASHIN_PATTERN = "GET_CATEGORY_ORANGE_CASHIN_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_RECEIVE_PATTERN =
        "GET_CATEGORY_ORANGE_RECEIVE_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_BALANCE_PATTERN =
        "GET_CATEGORY_ORANGE_BALANCE_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_CASHOUT_PATTERN =
        "GET_CATEGORY_ORANGE_CASHOUT_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_PAYMENTS_PATTERN =
        "GET_CATEGORY_ORANGE_PAYMENTS_PATTERN"
    private const val KEY_GET_TRANSACTION_ID_ORANGE_ID_TRANSACTION_PATTERN =
        "GET_TRANSACTION_ID_ORANGE_ID_TRANSACTION_PATTERN"
    private const val KEY_GET_TRANSACTION_ID_MTN_TRANSACTION_ID_PATTERN =
        "GET_TRANSACTION_ID_MTN_TRANSACTION_ID_PATTERN"

    private const val KEY_PREPROCESSING_REFERENCE_REMOVER_PATTERN =
        "PREPROCESSING_REFERENCE_REMOVER_PATTERN"
    private const val KEY_PREPROCESSING_USSD_CODE_PATTERN = "PREPROCESSING_USSD_CODE_PATTERN"
    private const val KEY_PREPROCESSING_SPECIAL_CARACTERS_PATTERN =
        "PREPROCESSING_SPECIAL_CARACTERS_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_INTEROPERABILITY_TRANSFERT_PATTERN =
        "GET_CATEGORY_ORANGE_INTEROPERABILITY_TRANSFERT_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_INTERNET_PATTERN =
        "GET_CATEGORY_ORANGE_INTERNET_PATTERN"
    private const val KEY_REMOVE_BONUS_BONUS_PATTERN = "REMOVE_BONUS_BONUS_PATTERN"
    private const val KEY_IS_FAILED_MESSAGE_KEY_WORDS_FAILED_MESSAGE =
        "IS_FAILED_MESSAGE_KEY_WORDS_FAILED_MESSAGE"
    private const val KEY_GET_CATEGORY_MTN_TRANSFER_PATTERN = "GET_CATEGORY_MTN_TRANSFER_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_CASHOUT_PATTERN = "GET_CATEGORY_MTN_CASHOUT_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_THIRD_PARTY_PAYMENT_PATTERN =
        "GET_CATEGORY_MTN_THIRD_PARTY_PAYMENT_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_CASHIN_PATTERN = "GET_CATEGORY_MTN_CASHIN_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_RECEIVE_PATTERN = "GET_CATEGORY_MTN_RECEIVE_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_INTERNET_PATTERN = "GET_CATEGORY_MTN_INTERNET_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_AIRTIME_PATTERN = "GET_CATEGORY_MTN_AIRTIME_PATTERN"
    private const val KEY_GET_CATEGORY_MTN_BALANCE_PATTERN = "GET_CATEGORY_MTN_BALANCE_PATTERN"
    private const val KEY_IS_RECEIVE_INTEROPERABILITY_MTN_RECEIVED_PATTERN =
        "IS_RECEIVE_INTEROPERABILITY_MTN_RECEIVED_PATTERN"
    private const val KEY_IS_RECEIVE_INTEROPERABILITY_MTN_MOMO_PATTERN =
        "IS_RECEIVE_INTEROPERABILITY_MTN_MOMO_PATTERN"
    private const val KEY_IS_INTEROPERABILITY_TRANSFER_MTN_TRANSFER_OR_PAYMENT =
        "IS_INTEROPERABILITY_TRANSFER_MTN_TRANSFER_OR_PAYMENT"
    private const val KEY_IS_INTEROPERABILITY_TRANSFERT_MTN_NON_MOMO_PATTERN =
        "IS_INTEROPERABILITY_TRANSFERT_MTN_NON_MOMO_PATTERN"
    private const val KEY_IS_RECEIVE_INTEROPERABILITY_ORANGE_RECEIVE_PATTERN =
        "IS_RECEIVE_INTEROPERABILITY_ORANGE_RECEIVE_PATTERN"
    private const val KEY_GET_TRANSACTION_AMOUNT_ORANGE_AMOUNT_MESSAGE =
        "GET_TRANSACTION_AMOUNT_ORANGE_AMOUNT_MESSAGE"
    private const val KEY_GET_COMMISSION_COMMISSION_MESSAGE_PATTERN =
        "GET_COMMISSION_COMMISSION_MESSAGE_PATTERN"
    private const val KEY_NUMERICAL_VALUES_PATTERN = "NUMERICAL_VALUES_PATTERN"
    private const val KEY_GET_FEES_FEES_MESSAGE_PATTERN = "GET_FEES_FEES_MESSAGE_PATTERN"
    private const val KEY_GET_BALANCE_BALANCE_MESSAGE_PATTERN =
        "GET_BALANCE_BALANCE_MESSAGE_PATTERN"
    private const val KEY_GET_CATEGORY_ORANGE_REMOVE_RECEIVE_DATA_PATTERN =
        "GET_CATEGORY_ORANGE_REMOVE_RECEIVE_DATA_PATTERN"
    private const val KEY_GET_TRANSACTION_AMOUNT_MTN_TRANSACTION_AMOUNT_PATTERN =
        "GET_TRANSACTION_AMOUNT_MTN_TRANSACTION_AMOUNT_PATTERN"

    private const val KEY_GET_EXTRACT_INFORMATIONS_FROM_SMS_VALUE_TO_EXCLUD_PATTERN =
        "GET_EXTRACT_INFORMATIONS_FROM_SMS_VALUE_TO_EXCLUD_PATTERN"

    private const val KEY_GET_CATEGORY_ORANGE_BILLS_PAIEMENTS_PATTERN =
        "GET_CATEGORY_ORANGE_BILLS_PAIEMENTS_PATTERN"

    private const val KEY_GET_CATEGORY_MTN_BILLS_PAIEMENTS_PATTERN =
        "GET_CATEGORY_MTN_BILLS_PAIEMENTS_PATTERN"

    private val remoteConfig: FirebaseRemoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    @SuppressLint("LongLogTag")
    fun fetchRemoteConfigFromServer() {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_sms_config_default)


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

    fun get_Preprocessing_IdTransactionOrange_Pattern(): String {
        return remoteConfig.getString(
            KEY_PREPROCESSING_ID_TRANSACTION_ORANGE_PATTERN
        )
    }

    fun get_Preprocessing_Date_Pattern(): String {
        return remoteConfig.getString(
            KEY_PREPROCESSING_DATE_PATTERN
        )
    }

    fun get_GetCategoryOrange_Cashin_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_CASHIN_PATTERN
        )
    }

    fun get_GetCategoryOrange_Receive_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_RECEIVE_PATTERN
        )
    }

    fun get_GetCategoryOrange_Balance_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_BALANCE_PATTERN
        )
    }

    fun get_GetCategoryOrange_Cashout_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_CASHOUT_PATTERN
        )
    }

    fun get_GetCategoryOrange_Payment_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_PAYMENTS_PATTERN
        )
    }

    fun get_GetTransactionIdOrange_IdTransaction_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_TRANSACTION_ID_ORANGE_ID_TRANSACTION_PATTERN
        )
    }

    fun get_GetTransactionIdMTN_TransactionId_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_TRANSACTION_ID_MTN_TRANSACTION_ID_PATTERN
        )
    }

    fun get_Preprocessing_ReferenceRemover_Pattern(): String {
        return remoteConfig.getString(
            KEY_PREPROCESSING_REFERENCE_REMOVER_PATTERN
        )
    }

    fun get_Preprocessing_USSDCode_Pattern(): String {
        return remoteConfig.getString(
            KEY_PREPROCESSING_USSD_CODE_PATTERN
        )
    }

    fun get_Preprocessing_SpecialCaracters_Pattern(): String {
        return remoteConfig.getString(
            KEY_PREPROCESSING_SPECIAL_CARACTERS_PATTERN
        )
    }

    fun get_GetCategoryOrange_InteroperabilityTransfert_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_INTEROPERABILITY_TRANSFERT_PATTERN
        )
    }

    fun get_GetCategoryOrange_Internet_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_ORANGE_INTERNET_PATTERN
        )
    }

    fun get_RemoveBonus_Bonus_Pattern(): String {
        return remoteConfig.getString(
            KEY_REMOVE_BONUS_BONUS_PATTERN
        )
    }

    fun get_IsFailedMessage_KeyWordsFailedMessage(): String {
        return remoteConfig.getString(
            KEY_IS_FAILED_MESSAGE_KEY_WORDS_FAILED_MESSAGE
        )
    }

    fun get_GetCategoryMTN_Transfer_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_TRANSFER_PATTERN
        )
    }

    fun get_GetCategoryMTN_Cashout_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_CASHOUT_PATTERN
        )
    }

    fun get_GetCategoryMTN_ThirdPartyPayment_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_THIRD_PARTY_PAYMENT_PATTERN
        )
    }

    fun get_GetCategoryMTN_Cashin_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_CASHIN_PATTERN
        )
    }

    fun get_GetCategoryMTN_Receive_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_RECEIVE_PATTERN
        )
    }

    fun get_GetCategoryMTN_Internet_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_INTERNET_PATTERN
        )
    }

    fun get_GetCategoryMTN_Airtime_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_AIRTIME_PATTERN
        )
    }

    fun get_GetCategoryMTN_Balance_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_CATEGORY_MTN_BALANCE_PATTERN
        )
    }

    fun get_IsReceiveInteroperabilityMTN_Receive_Pattern(): String {
        return remoteConfig.getString(
            KEY_IS_RECEIVE_INTEROPERABILITY_MTN_RECEIVED_PATTERN
        )
    }

    fun get_IsReceiveInteroperabilityMTN_MOMO_Pattern(): String {
        return remoteConfig.getString(
            KEY_IS_RECEIVE_INTEROPERABILITY_MTN_MOMO_PATTERN
        )
    }

    fun get_IsInteroperabilityTransferMTN_TransferOrPayment_Pattern(): String {
        return remoteConfig.getString(
            KEY_IS_INTEROPERABILITY_TRANSFER_MTN_TRANSFER_OR_PAYMENT
        )
    }

    fun get_IsInteroperabilityTransferMTN_NonMOMO_Pattern(): String {
        return remoteConfig.getString(
            KEY_IS_INTEROPERABILITY_TRANSFERT_MTN_NON_MOMO_PATTERN
        )
    }

    fun get_IsReceiveInteroperability_OrangeReceive_Pattern(): String {
        return remoteConfig.getString(
            KEY_IS_RECEIVE_INTEROPERABILITY_ORANGE_RECEIVE_PATTERN
        )
    }

    fun get_GetTransactionAmountOrange_AmountMessage_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_TRANSACTION_AMOUNT_ORANGE_AMOUNT_MESSAGE
        )
    }

    fun get_GetCommission_CommissionMessage_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_COMMISSION_COMMISSION_MESSAGE_PATTERN
        )
    }

    fun getNumericalValuesPattern(): String {
        return remoteConfig.getString(
            KEY_NUMERICAL_VALUES_PATTERN
        )
    }

    fun get_GetFees_FeesMessage_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_FEES_FEES_MESSAGE_PATTERN
        )
    }

    fun get_GetBalance_BalanceMessage_Pattern(): String {
        return remoteConfig.getString(
            KEY_GET_BALANCE_BALANCE_MESSAGE_PATTERN
        )
    }

    fun get_GetCategoryOrange_RemoveReceiveData_Pattern(): String {
        return remoteConfig.getString(KEY_GET_CATEGORY_ORANGE_REMOVE_RECEIVE_DATA_PATTERN)
    }

    fun get_transactionAmountMtn_TransactionAmount_Pattern(): String {
        return remoteConfig.getString(KEY_GET_TRANSACTION_AMOUNT_MTN_TRANSACTION_AMOUNT_PATTERN)
    }

    private fun get_extractInformation_from_sms_valueToExclud_pattern(): String {
        return remoteConfig.getString(KEY_GET_EXTRACT_INFORMATIONS_FROM_SMS_VALUE_TO_EXCLUD_PATTERN)
    }

    private fun get_getCategory_orange_bill_paiement(): String {
        return remoteConfig.getString(KEY_GET_CATEGORY_ORANGE_BILLS_PAIEMENTS_PATTERN)
    }

    private fun get_getCategory_mtn_bill_paiement(): String {
        return remoteConfig.getString(KEY_GET_CATEGORY_MTN_BILLS_PAIEMENTS_PATTERN)
    }


    fun setRegexFromRemote() {
        RegexConfig.VALUE_PREPROCESSING_ID_TRANSACTION_ORANGE_PATTERN =
            get_Preprocessing_IdTransactionOrange_Pattern()
        RegexConfig.VALUE_PREPROCESSING_DATE_PATTERN =
            get_Preprocessing_Date_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_CASHIN_PATTERN =
            get_GetCategoryOrange_Cashin_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_RECEIVE_PATTERN =
            get_GetCategoryOrange_Receive_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_BALANCE_PATTERN =
            get_GetCategoryOrange_Balance_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_CASHOUT_PATTERN =
            get_GetCategoryOrange_Cashout_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_PAYMENTS_PATTERN =
            get_GetCategoryOrange_Payment_Pattern()
        RegexConfig.VALUE_GET_TRANSACTION_ID_ORANGE_ID_TRANSACTION_PATTERN =
            get_GetTransactionIdOrange_IdTransaction_Pattern()
        RegexConfig.VALUE_GET_TRANSACTION_ID_MTN_TRANSACTION_ID_PATTERN =
            get_GetTransactionIdMTN_TransactionId_Pattern()
        RegexConfig.VALUE_PREPROCESSING_REFERENCE_REMOVER_PATTERN =
            get_Preprocessing_ReferenceRemover_Pattern()
        RegexConfig.VALUE_PREPROCESSING_USSD_CODE_PATTERN =
            get_Preprocessing_USSDCode_Pattern()
        RegexConfig.VALUE_PREPROCESSING_SPECIAL_CARACTERS_PATTERN =
            get_Preprocessing_SpecialCaracters_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_INTEROPERABILITY_TRANSFERT_PATTERN =
            get_GetCategoryOrange_InteroperabilityTransfert_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_INTERNET_PATTERN =
            get_GetCategoryOrange_Internet_Pattern()
        RegexConfig.VALUE_REMOVE_BONUS_BONUS_PATTERN =
            get_RemoveBonus_Bonus_Pattern()
        RegexConfig.VALUE_IS_FAILED_MESSAGE_KEY_WORDS_FAILED_MESSAGE =
            get_IsFailedMessage_KeyWordsFailedMessage()
        RegexConfig.VALUE_GET_CATEGORY_MTN_TRANSFER_PATTERN =
            get_GetCategoryMTN_Transfer_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_CASHOUT_PATTERN =
            get_GetCategoryMTN_Cashout_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_THIRD_PARTY_PAYMENT_PATTERN =
            get_GetCategoryMTN_ThirdPartyPayment_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_CASHIN_PATTERN =
            get_GetCategoryMTN_Cashin_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_RECEIVE_PATTERN =
            get_GetCategoryMTN_Receive_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_INTERNET_PATTERN =
            get_GetCategoryMTN_Internet_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_AIRTIME_PATTERN =
            get_GetCategoryMTN_Airtime_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_MTN_BALANCE_PATTERN =
            get_GetCategoryMTN_Balance_Pattern()
        RegexConfig.VALUE_IS_RECEIVE_INTEROPERABILITY_MTN_RECEIVED_PATTERN =
            get_IsReceiveInteroperabilityMTN_Receive_Pattern()
        RegexConfig.VALUE_IS_RECEIVE_INTEROPERABILITY_MTN_MOMO_PATTERN =
            get_IsReceiveInteroperabilityMTN_MOMO_Pattern()
        RegexConfig.VALUE_IS_INTEROPERABILITY_TRANSFER_MTN_TRANSFER_OR_PAYMENT =
            get_IsInteroperabilityTransferMTN_TransferOrPayment_Pattern()
        RegexConfig.VALUE_IS_INTEROPERABILITY_TRANSFERT_MTN_NON_MOMO_PATTERN =
            get_IsInteroperabilityTransferMTN_NonMOMO_Pattern()
        RegexConfig.VALUE_IS_RECEIVE_INTEROPERABILITY_ORANGE_RECEIVE_PATTERN =
            get_IsReceiveInteroperability_OrangeReceive_Pattern()
        RegexConfig.VALUE_GET_TRANSACTION_AMOUNT_ORANGE_AMOUNT_MESSAGE =
            get_GetTransactionAmountOrange_AmountMessage_Pattern()
        RegexConfig.VALUE_GET_COMMISSION_COMMISSION_MESSAGE_PATTERN =
            get_GetCommission_CommissionMessage_Pattern()
        RegexConfig.VALUE_NUMERICAL_VALUES_PATTERN =
            getNumericalValuesPattern()
        RegexConfig.VALUE_GET_FEES_FEES_MESSAGE_PATTERN =
            get_GetFees_FeesMessage_Pattern()
        RegexConfig.VALUE_GET_BALANCE_BALANCE_MESSAGE_PATTERN =
            get_GetBalance_BalanceMessage_Pattern()
        RegexConfig.VALUE_GET_CATEGORY_ORANGE_REMOVE_RECEIVE_DATA_PATTERN =
            get_GetCategoryOrange_RemoveReceiveData_Pattern()

        RegexConfig.VALUE_GET_TRANSACTION_AMOUNT_MTN_TRANSACTION_AMOUNT_PATTERN =
            get_transactionAmountMtn_TransactionAmount_Pattern()

        RegexConfig.VALUE_GET_EXTRACT_INFORMATIONS_FROM_SMS_VALUE_TO_EXCLUD_PATTERN =
            get_extractInformation_from_sms_valueToExclud_pattern()

        RegexConfig.VALUE_GET_CATEGORY_ORANGE_BILLS_PAIEMENTS_PATTERN =
            get_getCategory_orange_bill_paiement()

        RegexConfig.VALUE_GET_CATEGORY_MTN_BILLS_PAIEMENTS_PATTERN =
            get_getCategory_mtn_bill_paiement()
    }
}