package com.example.gaugeapp.commonRepositories

import android.util.Log
import java.util.*

object USSDFirestoreUtil {

    fun addErrormessage (message : String){
        FireStoreCollDocRef.errorMessage.add(Message(message, Calendar.getInstance().time)).addOnSuccessListener {
            Log.d("USSDFirestoreUtil", "add Error message successfully")
        }.addOnFailureListener {
            Log.d("USSDFirestoreUtil", "add Error message failed")
        }
    }

}

data class Message (val message: String, val date: Date)