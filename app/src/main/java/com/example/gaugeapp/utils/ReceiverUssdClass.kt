package com.example.gaugeapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.ussdlibrary.cons.USSDConst
import com.example.ussdlibrary.services.USSDService

class ReceiverUssdClass(val context: Context, val fonction: (intent: Intent) -> Unit) :
    BroadcastReceiver() {

    val TAG2 = "USSDServiceUnregister"
    /**
     * @french
     * initialisation de l'intent pour acceder au service USSD
     *
     */
    /**
     * @english
     * this initialization of the intent to access the USSD service
     */
    private var intentService: Intent = Intent(context, USSDService::class.java)

    init {
        /*try {
            context.unregisterReceiver(this)
            Log.d(TAG2, "Unregiter succes")
        } catch (e: Exception) {
        }*/
        context!!.startService(intentService)
        /**
         * @french
         * initialisation du broadcast receiver permettant d'ecouter sur le service USSD
         *
         */
        /**
         * @english
         * initialization of the Broacast receiver permitting to listen to the USSD
         */
        val filter = IntentFilter(USSDConst.keyBroadCastReceiverListen)
        context!!.registerReceiver(this, filter)
    }

    val TAG = "USSDServiceTrans"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "ici")

        val message = intent.getStringExtra(USSDConst.keyExtraMessage)

        if (isOrangeMoneyTransfertInitiated(message!!) || isMTNMoneyTransfertInitiated(message)) {
            try {
                fonction(intentService)
                Log.d(TAG, "Transaction enregistrée avec succès")
            } catch (ex: Exception) {
                context.unregisterReceiver(this)
            }
        } /*else {
            fonction(intentService)
            Log.d(TAG, "transaction non initiee")
        }*/

        if (message != null) {
            Log.d(TAG, message)
        }
    }

}