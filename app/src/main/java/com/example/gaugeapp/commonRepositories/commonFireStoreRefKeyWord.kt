package com.example.gaugeapp.commonRepositories

import android.os.Environment

object commonFireStoreRefKeyWord {

    const val DELAIT_VALUE = 1
    const val MAX_TENTATIVE_COUNT = 5
    const val NO_IMAGE_FOUND = ""
    //const val NO_NAME_FOUND = "NO_NAME_FOUND"

    const val FEES = "FEES"
    const val USERS_COLLECTION_REF = "KUSERS"
    const val SMS_USERS_COLLECTION = "USER_SMS"
    const val FINANCIAL_EDUCATION = "FINANCIAL_EDUCATION"
    const val FINANCIAL_EDUCATION_COMMENTS = "COMMENTS"
    const val COUNTER_COLLECTION = "COUNTER"
    const val AIRTIME_CREDIT_REQUEST = "AIRTIME_CREDIT_REQUEST"
    const val SHOPPING_CREDIT_REQUEST = "SHOPPING_CREDIT_REQUEST"
    const val COUNTER_DOCUMENT = "COUNTER_DOC"

    const val ASSOCIATED_PHONE_NUMBERS = "ASSOCIATED_PHONE_NUMBERS"

    const val WRONGS_TRANSACTIONS = "WRONGS_TRANSACTIONS"
    const val REPORTED_TRANSACTIONS = "REPORTED_TRANSACTIONS"
    const val USER_WRONGS_TRANSACTIONS = "USER_WRONGS_TRANSACTIONS"
    const val USER_REPORTED_TRANSACTIONS = "USER_REPORTED_TRANSACTIONS"
    const val UNCATEGORISED_SMS_TRANSACTIONS = "UNCATEGORISED_SMS_TRANSACTIONS"
    const val USER_UNCATEGORISED_TRANSACTIONS = "USER_UNCATEGORISED_TRANSACTIONS"
    const val BILLS_TRANSACTIONS ="BILLS_TRANSACTIONS"
    const val BILLS_TRANSACTIONS_OPERATORS ="BILLS_TRANSACTIONS_OPERATORS"
    const val FAILED_TRANSACTIONS_USERS ="FAILED_TRANSACTIONS_USERS"
    const val FAILED_TRANSACTIONS ="FAILED_TRANSACTIONS"
    const val PHONENUMBERS_AND_DETECTED_NAMES = "PHONENUMBERS_AND_DETECTED_NAMES"
    const val DETECTED_NAMES = "DETECTED_NAMES"
    const val ANOTHER_TRANSACTIONS = "ANOTHER_TRANSACTIONS"
    const val USER_ANOTHER_TRANSACTIONS = "USER_ANOTHER_TRANSACTIONS"

    const val PROMOTIONAL_CODE = "PROMOTIONAL_CODE"
    const val SAVED_CODE = "SAVED_CODE"
    const val GENERATED_CODE = "GENERATED_CODE"


    const val PROMOTIONAL_CODE_SAVED = "PROMOTIONAL_CODE_SAVED"
    const val GENERATED_CODE_SAVED = "GENERATED_CODE_SAVED"

    const val FIND_EDU_COMMENT = "FIND_EDU_COMMENT"
    const val FIN_EDU_SUB_COMMENT = "FIN_EDU_SUB_COMMENT"
    const val STORES = "SHOPS"

    const val KOLA_WALLET = "KOLA WALLET"

    const val ICOMES_DOCUMENT = "incomes"

    const val MESSAGES_COLLECTION_REF = "MESSAGES"

    const val AUDIO_EXTENSION = ".mp3"

    const val STORAGE_AUDIO_MESSAGE = "AudioMessages"
    const val STORAGE_IMAGE_MESSAGE = "ImagesMessages"
    const val STORAGE_FILE_MESSAGE = "FileMessages"
    const val STORAGE_IMAGE_PROFILE_PICTURE = "photos/profils_users"

    val STORAGE_LOCATION =
        "${Environment.getExternalStorageDirectory().absolutePath}/$KOLA_WALLET"
    val RECOR_RECEIVE_AUDIO_FOLDER = "$STORAGE_LOCATION/Audio/received"
    val RECOR_AUDIO_FOLDER = "$STORAGE_LOCATION/Audio"
    val KOLA_FACTURES_IMAGES = "$STORAGE_LOCATION/images"
    val RECOR_AUDIO_FOLDER_SENT = "$STORAGE_LOCATION/Audio/send"
    const val STORAGE_IMAGES_LOCATION = "$KOLA_WALLET/Kola factures pictures"
    const val SPLITOR = "SPLITOR_IMAGE_TEXT"

    val AUDIO_SEND_FOLDER = "$KOLA_WALLET/Audio/send"
    val AUDIO_RECEIVE_FOLDER = "$KOLA_WALLET/Audio/received"

    const val ROOM_DB_NAME = "kola_assistant_database"
    const val REMOTE_DB_STORAGE = "SQLITE_DB"

    const val TOTAL_INCOME = "TOTAL_INCOME"
    const val TOTAL_OUTCOME = "TOTAL_OUTCOME"

    const val SHOPPING_CREDIT_LINE = "SHOPPING_CREDIT_LINE"
    const val AIRTIME_CREDIT_LINE = "AIRTIME_CREDIT_LINE"
    const val CHAT_CHANNEL = "CHAT_CHANNEL"
    const val MESSAGES = "MESSAGES"

    const val CREDIT_LINE_COMMUNITY_LOAN = "CREDIT_LINE_COMMUNITY_LOAN"
    const val CAMPAIGN_COMMUNITY_LOAN = "CAMPAIGN_COMMUNITY_LOAN"
    const val SHOPPING_CREDITS = "SHOPPING_CREDITS"
    const val STORES_COLL_NAME = "STORES"
    const val FRAUDERS ="FRAUDERS"

}