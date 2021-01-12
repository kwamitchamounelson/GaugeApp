package com.example.gaugeapp.data.entities

import com.example.gaugeapp.utils.PhoneNumberUtils
import java.io.Serializable

class Contact(var phoneNumber: String, var contactName: String) : Serializable {
    constructor() : this("", "")

    override fun equals(other: Any?): Boolean {
        if (other is Contact) {
            /** @fench
             * on ajoute le 237 au numéro de téléphone
             * */
            /**
             * @english
             * we add the country code 237 the the phon number
             */
            if (PhoneNumberUtils.formatPhoneNumber(other.phoneNumber).equals(
                    PhoneNumberUtils.formatPhoneNumber(
                        this.phoneNumber
                    ), true
                )
            ) {
                return true
            }
        }
        return false
    }
}