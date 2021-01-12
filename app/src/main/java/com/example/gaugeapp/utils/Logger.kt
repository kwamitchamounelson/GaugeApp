package com.example.gaugeapp.utils

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.example.gaugeapp.utils.Constants.isUnitTest

object Constants {
    const val TAG = "AppDebug" // Tag for logs
    var DEBUG = true // enable logging
    var isUnitTest = false

}



fun printLogD(classTag: String?, message: String ) {
    if (Constants.DEBUG && !isUnitTest) {
        Log.d(Constants.TAG, "$classTag: $message")
    }
    else if(Constants.DEBUG && isUnitTest){
        println("$classTag: $message")
    }
}
/*
    Priorities: Log.DEBUG, Log. etc....
 */
fun cLog(msg: String?){
    msg?.let {
       // if(!Constants.DEBUG){
            FirebaseCrashlytics.getInstance().log(it)
        //}
    }
}