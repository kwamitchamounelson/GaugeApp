package com.example.gaugeapp.commonRepositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kola.kola_entities_features.entities.KUser
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.PhoneNumberUtils
import com.example.gaugeapp.utils.extentions.saveUserImageProfileInCash

object FireStoreAuthUtil {

    private val TAG = "FireStoreAuthUtil"


    fun getUserPhoneNumber(): String {
        return try {
            FirebaseAuth.getInstance().currentUser?.phoneNumber!!
        } catch (e: NullPointerException) {
            e.printStackTrace()
            ""
        }
    }

    fun getUserUID(): String {
        return FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("Uid number is null..")
    }

    fun getDetectedNameOfUser(
        phoneNumber: String,
        onSucess: (String) -> Unit,
        onError: () -> Unit
    ) {
        val phone = PhoneNumberUtils.remove237ToPhoneNumber(phoneNumber)
        val colOperator = when {
            PhoneNumberUtils.isOrangeOperator(phone) -> {
                ENUMOPERATEUR.ORANGE_MONEY.name
            }
            PhoneNumberUtils.isMTNOoperator(phone) -> {
                ENUMOPERATEUR.MTN_MONEY.name
            }
            else -> {
                "UNSUPORTED_OPERATOR"
            }
        }
        FireStoreCollDocRef.firestoreInstance
            .collection(commonFireStoreRefKeyWord.PHONENUMBERS_AND_DETECTED_NAMES)
            .document(colOperator)
            .collection(commonFireStoreRefKeyWord.DETECTED_NAMES)
            .document(phone)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    onSucess(document["detectedName"]?.toString() ?: "" as String)
                } else {
                    onError()
                }
            }
            .addOnFailureListener { exception ->
                onError()
            }
    }

    fun getUserFromFiresToreByAnyPhoneNumber(
        userPhoneNumber: String,
        onSucess: (KUser) -> Unit,
        onError: () -> Unit
    ) {
        FireStoreCollDocRef.firestoreInstance.collection(commonFireStoreRefKeyWord.USERS_COLLECTION_REF)
            .whereArrayContains(
                "phoneNumbers",
                PhoneNumberUtils.remove237ToPhoneNumber(userPhoneNumber)
            )
            .get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    val doc = documents.first()
                    var user = (doc).toObject(KUser::class.java)
                    user.userUid = doc.id
                    userPhoneNumber.saveUserImageProfileInCash(user.imageUrL)
                    onSucess(user)
                } else {
                    onError()
                }
            }
            .addOnFailureListener { exception ->
                onError()
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

}