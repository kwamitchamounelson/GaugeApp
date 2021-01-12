package com.example.gaugeapp.commonRepositories.smsRep

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef.curentUserDocRef
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef.firestoreInstance
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.ANOTHER_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.BILLS_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.BILLS_TRANSACTIONS_OPERATORS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.COUNTER_COLLECTION
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.COUNTER_DOCUMENT
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.DETECTED_NAMES
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.FAILED_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.FAILED_TRANSACTIONS_USERS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.PHONENUMBERS_AND_DETECTED_NAMES
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.REPORTED_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.SMS_USERS_COLLECTION
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.UNCATEGORISED_SMS_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.USER_ANOTHER_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.USER_REPORTED_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.USER_UNCATEGORISED_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.USER_WRONGS_TRANSACTIONS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.WRONGS_TRANSACTIONS
import com.example.gaugeapp.data.entities.CustumSmsPojo
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.MTN_OPERATOR_NAME
import com.example.gaugeapp.utils.ORANGE_OPERATOR_NAME
import com.example.gaugeapp.utils.PhoneNumberUtils
import com.example.gaugeapp.utils.extentions.addMonthOnDate
import com.example.gaugeapp.utils.extentions.getDetectedNameInCash
import com.example.gaugeapp.utils.extentions.saveDetectedNameInCash
import com.kola.smsmodule.entities.CustumSMS
import com.kola.smsmodule.entities.CustumSMSObjet
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS
import com.kola.smsmodule.enums.ENUM_SERVICE_OPERATEUR
import java.util.*

object SMSFirestoreRepository {

    private val TAG = "SMSFirestoreRepository"

    fun saveCustumSMSToFireStore(custumSms: CustumSMS, onSuccess: () -> Unit, onError: () -> Unit) {
        curentUserDocRef.collection(SMS_USERS_COLLECTION)
            .document(custumSms.custumSMSObjet!!.messageId).set(custumSms)
            .addOnSuccessListener {
                incrementSMSCount()

                //On sauvegarde les informations (noms et numéro de téléphone des intervenant que si
                // la transtion n'appartient pas à la catégorie ANOTHER_TRANSACTIONS_ORANGE et ANOTHER_TRANSACTIONS_MTN
                if ((custumSms.transaction?.transactionService != ENUM_SERVICE_ANALYSIS.ANOTHER_TRANSACTIONS_ORANGE)
                    && (custumSms.transaction?.transactionService != ENUM_SERVICE_ANALYSIS.ANOTHER_TRANSACTIONS_MTN)
                ) {
                    associateTransactionNameWithPhoneNumber(
                        custumSms.transaction!!.senderPhoneNumber,
                        custumSms.transaction!!.senderName
                    )
                    associateTransactionNameWithPhoneNumber(
                        custumSms.transaction!!.recipientPhoneNumber,
                        custumSms.transaction!!.recipientName
                    )

                    val senderPhone =
                        PhoneNumberUtils.remove237ToPhoneNumber(custumSms.transaction!!.senderPhoneNumber)

                    if (senderPhone == PhoneNumberUtils.remove237ToPhoneNumber(FireStoreAuthUtil.getUserPhoneNumber())) {
                        saveDetectedNameToUser(
                            custumSms.transaction!!.senderName
                        )
                    }
                }

                //Si le SMS a été analyser correctement on les supprime dans les autres collections au cas ou son analyse avait un problème au préalable
                deleteUncategorisedTransaction(custumSms.custumSMSObjet?.messageId ?: "")
                deleteWrongTransactionsToFireStore(custumSms, onSuccess = {}, onError = {})
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }

        if (custumSms.transaction?.transactionService == ENUM_SERVICE_ANALYSIS.ANOTHER_TRANSACTIONS_ORANGE
            || custumSms.transaction?.transactionService == ENUM_SERVICE_ANALYSIS.ANOTHER_TRANSACTIONS_MTN
        ) {
            saveAnotherTransactionsToFireStore(custumSms)
        }
    }

    fun saveAnotherTransactionsToFireStore(
        custumSms: CustumSMS
    ) {

        val dataToSave = mutableMapOf<String, Any>()
        dataToSave["custumSms"] = custumSms
        dataToSave["savedAt"] = FieldValue.serverTimestamp()

        custumSms.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(ANOTHER_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_ANOTHER_TRANSACTIONS)
            .document(custumSms.custumSMSObjet!!.messageId).set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "another transaction saved.")
            }
            .addOnFailureListener {
            }
    }


    const val IS_DETECTEDNAME_FROM_USSD = "isDetectedNameFromUSSD"
    private fun saveDetectedNameToUser(
        name: String
    ) {

        if (name.isNotEmpty()) {
            curentUserDocRef.get().addOnCompleteListener {
                val detectedNameIsUpdated = it.result?.getBoolean(IS_DETECTEDNAME_FROM_USSD)

                if (detectedNameIsUpdated ?: false) {
                    return@addOnCompleteListener
                } else {
                    curentUserDocRef.update("detectedName", name)
                }
            }
        }
    }

    fun saveDetectedNameFromUSSDToUser(name: String, phoneNumber: String) {

        val formattedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(phoneNumber)

        //save detected name in cash
        phoneNumber.saveDetectedNameInCash(name)

        FireStoreCollDocRef.UserColRef
            .whereEqualTo("authPhoneNumber", formattedPhoneNumber)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    return@addSnapshotListener
                }

                try {
                    querySnapshot?.documents?.forEach {
                        Log.d(TAG, "user detected")
                        val detectedNameIsUpdate = it.getBoolean(IS_DETECTEDNAME_FROM_USSD)

                        if (detectedNameIsUpdate ?: false) {
                            return@addSnapshotListener
                        } else {
                            val map = mutableMapOf<String, Any>()
                            map.put("detectedName", name)
                            map.put(IS_DETECTEDNAME_FROM_USSD, true)
                            it.reference.update(map)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    /**
     * Cette fonction a pour but d'associé le nom recupérer dans la transaction
     * au numéro de téléphone de l'utilisateur
     */
    fun associateTransactionNameWithPhoneNumber(
        phoneNumber: String,
        name: String,
        isFromUSSD: Boolean = false
    ) {
        val detectedNames = DETECTED_NAMES
        //For the sender
        val senderPhoneNumber =
            PhoneNumberUtils.remove237ToPhoneNumber(phoneNumber)
        try {

            //on regarde si le nom detecte est deja dans les cash, sinon on l'enregistre
            val detectedName = phoneNumber.getDetectedNameInCash()
            if (detectedName.isBlank()) {
                phoneNumber.saveDetectedNameInCash(name)
            }


            /*PerformTransferFragment.transactionInfos?.let {
                it.userName = name
                userPref.saveRecentContactTransaction(phoneNumber, name)
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("PerformTransferFragment", "Utils enter" + name + " " + senderPhoneNumber)
        when {
            PhoneNumberUtils.isOrangeOperator(senderPhoneNumber) -> {
                if (name.isNotEmpty()) {
                    val userFieldMap = mutableMapOf<String, Any>()
                    userFieldMap["phoneNumber"] = senderPhoneNumber
                    userFieldMap["detectedName"] = name
                    userFieldMap["isNameFromUSSD"] = isFromUSSD
                    /*        try {
                                userFieldMap["SavedDate"] = transaction.date!!
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }*/
//                    firestoreInstance.collection(PHONENUMBERS_AND_DETECTED_NAMES)
//                        .document(ENUM_OPERATEUR.ORANGE_MONEY.name).collection(detectedNames)
//                        .document(senderPhoneNumber).set(userFieldMap, SetOptions.merge())

                    val userRef = firestoreInstance.collection(PHONENUMBERS_AND_DETECTED_NAMES)
                        .document(ENUMOPERATEUR.ORANGE_MONEY.name).collection(detectedNames)
                        .document(senderPhoneNumber)

                    userRef
                        .get().addOnSuccessListener {
                            if (it.exists()) {
                                if (!(it["isNameFromUSSD"] == true)) {
                                    userRef.set(userFieldMap, SetOptions.merge())
                                } else if (isFromUSSD) {
                                    userRef.set(userFieldMap, SetOptions.merge())
                                }
                            } else {
                                userRef.set(userFieldMap, SetOptions.merge())
                            }
                        }
                }
            }
            PhoneNumberUtils.isMTNOoperator(senderPhoneNumber) -> {
                if (name.isNotEmpty()) {
                    val userFieldMap = mutableMapOf<String, Any>()
                    userFieldMap["phoneNumber"] = senderPhoneNumber
                    userFieldMap["detectedName"] = name
                    userFieldMap["isNameFromUSSD"] = isFromUSSD

                    /*         try {
                                 userFieldMap["SavedDate"] = transaction.date!!
                             } catch (ex: Exception) {
                                 ex.printStackTrace()
                             }*/
//                    firestoreInstance.collection(PHONENUMBERS_AND_DETECTED_NAMES)
//                        .document(ENUM_OPERATEUR.MTN_MONEY.name).collection(detectedNames)
//                        .document(senderPhoneNumber).set(userFieldMap, SetOptions.merge())

                    val userRef = firestoreInstance.collection(PHONENUMBERS_AND_DETECTED_NAMES)
                        .document(ENUMOPERATEUR.MTN_MONEY.name).collection(detectedNames)
                        .document(senderPhoneNumber)

                    Log.e("PerformTransferFragment", "Utils " + name + " " + phoneNumber)

                    userRef
                        .get().addOnSuccessListener {
                            if (it.exists()) {
                                if (!(it["isNameFromUSSD"] == true)) {
                                    userRef.set(userFieldMap, SetOptions.merge())
                                } else if (isFromUSSD) {
                                    userRef.set(userFieldMap, SetOptions.merge())
                                }
                            } else {
                                Log.e("PerformTransferFragment", "Don't exists")
                                userRef.set(userFieldMap, SetOptions.merge())
                            }
                        }
                }
            }
        }

    }


    fun saveFailedTransaction(custumSMSObjet: CustumSMSObjet) {

        val dataToSave = mutableMapOf<String, Any>()
        dataToSave["custumSmsObj"] = custumSMSObjet
        dataToSave["savedAt"] = FieldValue.serverTimestamp()

        firestoreInstance.collection(FAILED_TRANSACTIONS_USERS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(FAILED_TRANSACTIONS)
            .document(custumSMSObjet.messageId).set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "saveFailed Transaction transaction saved.")
            }
            .addOnFailureListener {
                Log.d(TAG, "Error to save saveFailed Transaction transaction.")
                it.printStackTrace()
            }
    }

    fun updateUserTransactionToServer(custumSms: CustumSMS) {
        curentUserDocRef
            .collection(SMS_USERS_COLLECTION)
            .document(custumSms.custumSMSObjet!!.messageId)
            .set(custumSms, SetOptions.merge())
            .addOnCompleteListener {}
    }

    /**
     * Get the transactions of latesd three months
     * */
    fun getAllUsersTransactionsFromServer(
        onSuccess: (ArrayList<CustumSMS>) -> Unit,
        onError: () -> Unit
    ) {
        // On recupère les transaction faites sur les 3 mois précedent
        val dateNow = Date()
        val dateThreeMontBefor = dateNow.addMonthOnDate(-3)
        curentUserDocRef
            .collection(SMS_USERS_COLLECTION)
            .orderBy("dateTransaction", Query.Direction.DESCENDING)
            .whereGreaterThan("dateTransaction", dateThreeMontBefor)
            .get()
            .addOnSuccessListener { documents ->

                val listOfCustumSms = kotlin.collections.ArrayList<CustumSMS>()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    try {
                        listOfCustumSms.add(document.toObject(CustumSMS::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                onSuccess(listOfCustumSms)

            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                onError()
            }
    }

    /**
     * Cette fonction a pour but d'incrémenter le nombre de transaction
     * à chaque fois qu'une nouvelle transaction est enregistré dans le serveur
     */
    private fun incrementSMSCount() {

        val map = mutableMapOf<String, Any>()
        map["smsTransactionCount"] = FieldValue.increment(1)

        curentUserDocRef.collection(COUNTER_COLLECTION).document(COUNTER_DOCUMENT)
            .set(map, SetOptions.merge())
        Log.d(TAG, "nombre de transaction mis à jours")
    }

    fun getSmsTransactionCount(onSuccess: (transactionNumber: Long) -> Unit) {

        curentUserDocRef.collection(COUNTER_COLLECTION).document(COUNTER_DOCUMENT).get()
            .addOnSuccessListener { userDocuentSnapShot ->
                try {
                    val transactionNumber = userDocuentSnapShot["smsTransactionCount"] as Long
                    Log.d(TAG, "All transaction smsTransactionCount is: $transactionNumber")
                    onSuccess(transactionNumber)
                } catch (e: Exception) {
                    onSuccess(0)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error to get all transaction smsTransactionCount $exception")
            }
    }

    fun saveUncategorisedTransaction(custumSMSObjet: CustumSMSObjet) {

        val dataToSave = mutableMapOf<String, Any>()
        dataToSave["custumSms"] = custumSMSObjet
        dataToSave["savedAt"] = FieldValue.serverTimestamp()

        firestoreInstance.collection(UNCATEGORISED_SMS_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_UNCATEGORISED_TRANSACTIONS)
            .document(custumSMSObjet.messageId).set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Uncategorised transaction saved.")
            }
            .addOnFailureListener {
                Log.d(TAG, "Error to save Uncategorised transaction.")
                it.printStackTrace()
            }
    }

    fun saveWrongTransactionForBillsToFireStore(
        custumSms: CustumSMS,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {

        val dataToSave = mutableMapOf<String, Any>()
        dataToSave["custumSms"] = custumSms
        dataToSave["savedAt"] = FieldValue.serverTimestamp()

        val operator = when (custumSms.serviceOperateur?.name) {
            ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY.name -> {
                ORANGE_OPERATOR_NAME
            }
            ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY.name -> {
                MTN_OPERATOR_NAME
            }
            else -> {
                "UNKNOW_OPERATOR"
            }
        }

        custumSms.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(BILLS_TRANSACTIONS_OPERATORS)
            .document(operator)
            .collection(BILLS_TRANSACTIONS)
            .document(custumSms.custumSMSObjet!!.messageId).set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "new Bill transaction saved.")
                // incrementSMSCount()
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun saveReportedTransactionsToFireStore(
        custumSms: CustumSMS,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {

        val dataToSave = mutableMapOf<String, Any>()
        dataToSave["custumSms"] = custumSms
        dataToSave["savedAt"] = FieldValue.serverTimestamp()

        custumSms.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(REPORTED_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_REPORTED_TRANSACTIONS)
            .document(custumSms.custumSMSObjet!!.messageId).set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "reported transaction saved.")
                // incrementSMSCount()
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun saveWrongTransactionToFireStore(
        custumSms: CustumSMS,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {

        val dataToSave = mutableMapOf<String, Any>()
        dataToSave["custumSms"] = custumSms
        dataToSave["savedAt"] = FieldValue.serverTimestamp()

        custumSms.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(WRONGS_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_WRONGS_TRANSACTIONS)
            .document(custumSms.custumSMSObjet!!.messageId).set(dataToSave, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Wrong transaction saved.")
                // incrementSMSCount()
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun getUserWrongTransactions(
        onSuccess: (List<CustumSMS>) -> Unit,
        onError: () -> Unit
    ) {
        firestoreInstance.collection(WRONGS_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_WRONGS_TRANSACTIONS)
            .get()
            .addOnSuccessListener {
                var result = arrayListOf<CustumSMS>()
                it.forEach {
                    try {
                        val another = it.toObject(CustumSmsPojo::class.java)
                        result.add(another.custumSms)

                        Log.d(
                            TAG,
                            "result *********************************${another}"
                        )
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                        Log.d(TAG, " ----------------------------------- ")
                        onError()
                    }
                }
                onSuccess(result)
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun getAnotherTransactionsToFireStore(
        onSuccess: (List<CustumSMS>) -> Unit,
        onError: () -> Unit
    ) {

        firestoreInstance.collection(ANOTHER_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_ANOTHER_TRANSACTIONS)
            .get()
            .addOnSuccessListener {

                var result = arrayListOf<CustumSMS>()
                it.forEach {
                    try {
                        val another = it.toObject(CustumSmsPojo::class.java)
                        result.add(another.custumSms)

                        Log.d(
                            TAG,
                            "result *********************************${another}"
                        )
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                        Log.d(TAG, " ----------------------------------- ")
                        onError()
                    }
                }
                onSuccess(result)
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun getReportedTransactionsToFireStore(
        onSuccess: (List<CustumSMS>) -> Unit,
        onError: () -> Unit
    ) {

        firestoreInstance.collection(REPORTED_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_REPORTED_TRANSACTIONS)
            .get()
            .addOnSuccessListener {
                Log.d(TAG, "Wrong transaction saved.")
                // incrementSMSCount()
                var result = arrayListOf<CustumSMS>()
                it.forEach {
                    try {
                        val reported = it.toObject(CustumSmsPojo::class.java)
                        result.add(reported.custumSms)

                        Log.d(
                            TAG,
                            "result *********************************${reported}"
                        )
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                        Log.d(TAG, " ----------------------------------- ")
                        onError()
                    }
                }
                onSuccess(result)
            }
            .addOnFailureListener {
                onError()
            }
    }


    fun deleteWrongTransactionForBillsToFireStore(
        custumSms: CustumSMS
    ) {

        val operator = when (custumSms.serviceOperateur?.name) {
            ENUM_SERVICE_OPERATEUR.SERVICE_ORANGE_MONEY.name -> {
                ORANGE_OPERATOR_NAME
            }
            ENUM_SERVICE_OPERATEUR.SERVICE_MTN_MONEY.name -> {
                MTN_OPERATOR_NAME
            }
            else -> {
                "UNKNOW_OPERATOR"
            }
        }

        custumSms.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(BILLS_TRANSACTIONS_OPERATORS)
            .document(operator)
            .collection(BILLS_TRANSACTIONS)
            .document(custumSms.custumSMSObjet!!.messageId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "new Bill transaction deleted.")

            }
            .addOnFailureListener {
            }
    }

    fun deleteUncategorisedTransaction(transactionId: String) {

        firestoreInstance.collection(UNCATEGORISED_SMS_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_UNCATEGORISED_TRANSACTIONS)
            .document(transactionId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Uncategorised transaction deleted.")
            }
            .addOnFailureListener {
                Log.d(TAG, "Error to delete Uncategorised transaction.")
                it.printStackTrace()
            }
    }

    fun deleteReportedTransactionsToFireStore(
        custumSms: CustumSMS
    ) {

        custumSms.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(REPORTED_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_REPORTED_TRANSACTIONS)
            .document(custumSms.custumSMSObjet!!.messageId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Wrong transaction saved.")
            }
            .addOnFailureListener {
            }
    }

    fun deleteAnotherTransactionsToFireStore(
        currentCustumSMS: CustumSMS,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        currentCustumSMS.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(ANOTHER_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_ANOTHER_TRANSACTIONS)
            .document(currentCustumSMS.custumSMSObjet!!.messageId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Wrong transaction saved.")
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }

    fun deleteWrongTransactionsToFireStore(
        currentCustumSMS: CustumSMS,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        currentCustumSMS.userid = FireStoreAuthUtil.getUserUID()
        firestoreInstance.collection(WRONGS_TRANSACTIONS)
            .document(FireStoreAuthUtil.getUserUID())
            .collection(USER_WRONGS_TRANSACTIONS)
            .document(currentCustumSMS.custumSMSObjet!!.messageId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "Wrong transaction saved.")
                onSuccess()
            }
            .addOnFailureListener {
                onError()
            }
    }
}