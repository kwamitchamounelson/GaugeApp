package com.example.gaugeapp.utils

object PromotionUtils {

    fun generatePromotionCodes(numberOfCode: Int): List<String> {
        val codeList = mutableListOf<String>()
        (0..numberOfCode).forEach {

            var tempCode = ""
            do {
                tempCode = "${getRandomString(3)}${getRandomInt(4)}"
                //println(tempCode)
            } while (codeList.contains(tempCode))
            codeList.add(tempCode)
        }
        return codeList
    }


    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') //+ ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getRandomInt(length: Int): String {
        val allowedChars = ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}