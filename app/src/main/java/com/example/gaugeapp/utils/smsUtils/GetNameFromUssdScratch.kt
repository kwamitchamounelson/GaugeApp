package com.example.gaugeapp.utils.smsUtils

import com.kola.smsmodule.util.RegexConfig

object GetNameFromUssdScratch {

    val OrangeMessageFrench =
        """Transfert de 300 FCFA à 690582957 landry noulawe. Frais: 3FCFA. Entrez
    | votre code secret pour confirmer ou 2 pour annuler""".trimMargin()

    val OrangeMessageEnglish =
        """Money Transfer of 50 FCFA to 690582955 landry noulawe. FEES: 0.5FCFA.
    |Please enter your secret code to confirm or press 2 to cancel""".trimMargin()

    val MtnMessageFrench = """Confirmez le transfert de 100
FCFA a Zoonyaba Parente
Loic(237672746645).
1) OUI
2) NON"""

    val MtnMessageEnglish = """Confirm money transfer of FCFA 50 to
237672746645 Zoonyaba Parente Loic
1) Yes
2) No"""


    fun preprocessingForUssdMessages(ussdMessage: String): String {

        val regexForSpecialCaracters =
            RegexConfig.VALUE_PREPROCESSING_SPECIAL_CARACTERS_PATTERN.toRegex() //"""[^\w \,\.]"""

        var message = ussdMessage.toUpperCase()

        //on retir les caractères spéciaux
        message = message.replace(regexForSpecialCaracters, " ")
        //on retir le A, les parenthèses ouvrantes et fermentes et les FCFA
        message = message.replace("""(\bA\b|\(|\)|FCFA)""".toRegex(), " ")
        return message
    }

    fun getPhoneNumberFromRegex (message : String) : String{
        val phoneNumberRegex = """(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()

        val phoneNumberRegexFinder = phoneNumberRegex.find(message)

        val phoneNumberValue = phoneNumberRegexFinder?.value

        return phoneNumberValue ?: ""
    }

    fun getNameFromOrangeUSSD(ussdMessage: String): String {

        val message = ussdMessage.toUpperCase()

        val phoneNumberRegex = """(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()
        val feesRegex = """\.?\s*(FRAIS|FEES)""".toRegex()


        val phoneNumberRegexFinder = phoneNumberRegex.find(message)
        val phoneNumberRangeEnd = phoneNumberRegexFinder?.range?.last

        val feesRegexFinder = feesRegex.find(message)
        val feesRangeStart = feesRegexFinder?.range?.first

        val startIndex = (phoneNumberRangeEnd ?: 0) + 1
        val endIndex = (feesRangeStart ?: 1)

        val findedName = message.subSequence(startIndex, endIndex)

        return findedName.toString().trim()
    }

    fun getNameFromMtnUSSD(ussdMessage: String, frenchLanguage: Boolean): String {
        var message = ussdMessage.toUpperCase()

        val numericalValueRegex = """\d+(\.\d+)?""".toRegex()

        val phoneNumberRegex = """(237)?6(9|7|5|8)[0-9]{7}\s*""".toRegex()

        //French language
        if (frenchLanguage) {

            //on retir le A et les parenthèses
            message = preprocessingForUssdMessages(message)

            val numericalValueFinder = numericalValueRegex.find(message)
            val phoneNumberRegexFinder = phoneNumberRegex.find(message)

            val numericalValueRangeEnd = numericalValueFinder?.range?.last
            val phoneNumberRangeStart = phoneNumberRegexFinder?.range?.first

            val startIndex = (numericalValueRangeEnd ?: 0) + 1
            val endIndex = (phoneNumberRangeStart ?: 1)

            val findedName = message.subSequence(startIndex, endIndex)

            return findedName.toString().trim()

        }

        //english language
        if (!frenchLanguage) {

            val endNameRegex = """\s*1\)""".toRegex()

            val phoneNumberValueFinder = phoneNumberRegex.find(message)
            val endNameValueFinder = endNameRegex.find(message)

            val phoneNumberRangeEnd = phoneNumberValueFinder?.range?.last
            val endNameRangeStart = endNameValueFinder?.range?.first

            val startIndex = (phoneNumberRangeEnd ?: 0) + 1
            val endIndex = (endNameRangeStart ?: 1)

            val findedName = message.subSequence(startIndex, endIndex)

            return findedName.toString().trim()
        }

        return ""
    }

    val nameFromMessage = getNameFromOrangeUSSD(OrangeMessageFrench)

    //println("Original message: $MtnMessageFrench\n\n")
    //println("Name: ${nameFromMessage}")

//println("preproced message: ${preprocessingForUssdMessages(MtnMessageFrench)}")


}