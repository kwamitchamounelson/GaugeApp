package com.example.gaugeapp.commonRepositories

import com.google.firebase.firestore.SetOptions
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.FEES
import com.example.gaugeapp.data.entities.FeesTransaction

object FreeTransferUtil {

    //to save fee
    fun saveFees(fee: FeesTransaction, onSucess: () -> Unit, onError: () -> Unit) {

        FireStoreCollDocRef.firestoreInstance.collection(FEES).document(fee.name)
            .set(fee, SetOptions.merge())
            .addOnSuccessListener {
                onSucess()
            }
            .addOnFailureListener {
                onError()
            }
    }
}