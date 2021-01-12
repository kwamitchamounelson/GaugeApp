package com.example.gaugeapp.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.*
import com.kola.kola_entities_features.entities.KUser
import com.kola.smsmodule.entities.CustumSMS
import java.util.*


object TransactionGsonConverter {

    fun convertCustumSmsToGson(custumSms: CustumSMS): String {

        val gson = GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                jsonSerializer
            )
            .registerTypeAdapter(
                Date::class.java,
                jsonDeserializer
            )
            .create()

        return gson.toJson(custumSms)
    }

    fun getCustumSmsFromGson(jsonString: String): CustumSMS? {

        val gson = GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                jsonSerializer
            )
            .registerTypeAdapter(
                Date::class.java,
                jsonDeserializer
            )
            .create()

        return try {
            val custumSms = gson.fromJson(jsonString, CustumSMS::class.java)
            custumSms.apply {
                transaction?.date = custumSMSObjet?.dateReceive
                dateTransaction = custumSMSObjet?.dateReceive
                custumSMSObjet?.dateSend = custumSMSObjet?.dateReceive
            }
        } catch (ex: JsonSyntaxException) {
            FirebaseCrashlytics.getInstance().recordException(ex)
            ex.printStackTrace()
            null
        }

/*        return try {
            gson.fromJson(jsonString, CustumSMS::class.java)
        } catch (ex: JsonSyntaxException) {
            ex.printStackTrace()
            try {
                gson.fromJson(jsonString, CustumSMS::class.java)
            } catch (ex: JsonSyntaxException) {
                ex.printStackTrace()
                try {
                    gson.fromJson(jsonString, CustumSMS::class.java)
                } catch (ex: JsonSyntaxException) {
                    ex.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(ex)
                    gson.fromJson(jsonString, CustumSMS::class.java)
                }
            }
        }*/
    }

    fun getUserFromJson(jsonString: String): KUser? {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                jsonSerializer
            )
            .registerTypeAdapter(
                Date::class.java,
                jsonDeserializer
            )
            .create()

        return try {
            gson.fromJson(jsonString, KUser::class.java)
        } catch (ex: JsonSyntaxException) {
            FirebaseCrashlytics.getInstance().recordException(ex)
            ex.printStackTrace()
            null
        }
    }

    fun convertUserToGson(user: KUser): String {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                jsonSerializer
            )
            .registerTypeAdapter(
                Date::class.java,
                jsonDeserializer
            )
            .create()

        return gson.toJson(user)
    }

    private var jsonSerializer: JsonSerializer<Date?> = JsonSerializer<Date?> { src, typeOfSrc, context ->
        if (src == null) {
            null
        } else {
            JsonPrimitive(
                src.time
            )
        }
    }

    private var jsonDeserializer: JsonDeserializer<Date?> =
        JsonDeserializer<Date?> { json, typeOfT, context ->
            if (json == null) null else Date(json.asLong)
        }


}