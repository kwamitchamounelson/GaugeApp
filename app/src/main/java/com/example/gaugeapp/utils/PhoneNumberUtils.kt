package com.example.gaugeapp.utils

import android.content.Context
import com.example.gaugeapp.R
import com.example.gaugeapp.utils.extentions.cleanPhoneNumber
import java.lang.NumberFormatException

object PhoneNumberUtils {

    /**
     * @french
     * code de détection de l'opérateur orange à partir d'un numéro de téléphone
     *
     */
    /**
     * @english
     * detect the operator from the phone number
     */
    // TODO: 08/08/2020 remove the later
    val testNumberList = arrayListOf<String>(
        "+237650787682",
        "+237698481558",
        "+237659405714",
        "+237694566544",
        "+237691621700",
        "+237791621708"
    )
    //var testingPhoneNumbers="+237750787681"

    fun isOrangeOperator(phone: String): Boolean {
        val phoneNumber = phone.cleanPhoneNumber()
        return if (phoneNumber.length == 9 && phoneNumber.first() == '6') {
            val firtsSubSeq = phoneNumber.subSequence(0, 2) as String
            val secondSubSeq = phoneNumber.subSequence(0, 3) as String
            (firtsSubSeq.toInt() == 69 || secondSubSeq.toInt() in 655..659)
        } else {
            false
            // testNumberList.contains(phone)
        }
    }

    /**
     * @french
     * code de détection de l'opérateur Mtn à partir d'un numéro de téléphone
     */
    /**
     * @english
     * detect the MTN phone Number
     */

    fun isMTNOoperator(phone: String): Boolean {
        val phoneNumber = phone.cleanPhoneNumber()
        return if (phoneNumber.length == 9 && phoneNumber.first() == '6') {
            val firtsSubSeq = phoneNumber.subSequence(0, 2) as String
            val secondSubSeq = phoneNumber.subSequence(0, 3) as String
            (firtsSubSeq.toInt() == 67 || (secondSubSeq.toInt() in 650..654) || secondSubSeq.toInt() in 680..684)
        } else {
            false
            //testNumberList.contains(phone)
        }
    }


    fun formatPhoneNumber(userPhoneNumber: String): String {
        var phoneStr = remove237ToPhoneNumber(userPhoneNumber)
        phoneStr = if (phoneStr.isNotEmpty()) {
            "+237$phoneStr"
        } else {
            userPhoneNumber
        }
        return phoneStr.replace("\\s".toRegex(), "")
    }

    /**
     * @french
     * appele quand on veut deja initier la transaction
     *
     */
    /**
     * @english
     * call when we want to initiate a transation , it removes the country code
     */
    fun remove237ToPhoneNumber(userPhoneNumber: String): String {
        if (userPhoneNumber.isNotEmpty()) {
            if (userPhoneNumber.startsWith("+237")) {
                return userPhoneNumber.removePrefix("+237")
            }
            if (userPhoneNumber.startsWith("237")) {
                return userPhoneNumber.removePrefix("237")
            }
        }
        return userPhoneNumber
    }

    fun isGreetPhoneNumber(phoneNumber: String): Boolean {
        return (isMTNOoperator(phoneNumber) || isOrangeOperator(phoneNumber))
    }

    //to manage display of contact name
    fun getGoodDisplayName(name: String, context: Context): String {
        return try {
            (name.replace("\\s".toRegex(), "")).toBigInteger()
            context.getString(R.string.unknown)
        } catch (e: NumberFormatException) {
            if (name.isEmpty()) {
                context.getString(R.string.unknown)
            } else {
                name
            }
        }
    }
}