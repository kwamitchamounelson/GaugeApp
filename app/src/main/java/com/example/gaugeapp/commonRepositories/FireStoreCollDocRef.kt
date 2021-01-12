package com.example.gaugeapp.commonRepositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.AIRTIME_CREDIT_LINE
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.CAMPAIGN_COMMUNITY_LOAN
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.CHAT_CHANNEL
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.CREDIT_LINE_COMMUNITY_LOAN
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.FINANCIAL_EDUCATION
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.FINANCIAL_EDUCATION_COMMENTS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.FRAUDERS
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.SHOPPING_CREDIT_LINE
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.STORES
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.STORES_COLL_NAME

object FireStoreCollDocRef {

    /* @Inject
     lateinit var firestoreInstance: FirebaseFirestore*/
    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    val curentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            commonFireStoreRefKeyWord.USERS_COLLECTION_REF.plus(
                "/${FireStoreAuthUtil.getUserUID()}"
            )
        )

    val UserColRef: CollectionReference
        get() = firestoreInstance.collection(commonFireStoreRefKeyWord.USERS_COLLECTION_REF)

    val kolaDoc = firestoreInstance.collection("Kola").document("Kola_wallet")
    val budgetColRef = curentUserDocRef.collection("BUDGET")
    val bookkeepingsColRef = curentUserDocRef.collection("BOOKKEEPING")
    val transactionsColRef = curentUserDocRef.collection("CUSTOM_SMS")
    val financialLearningCalRef = firestoreInstance.collection(FINANCIAL_EDUCATION)
    val financialLearningCommentsCalRef = firestoreInstance.collection(FINANCIAL_EDUCATION_COMMENTS)
    val storesColRef = firestoreInstance.collection(STORES)
    val errorMessage = firestoreInstance.collection("ERROR_MESSAGE_USSD")
    val CurrentUserDeviceInfoColRef = firestoreInstance.collection("USERS_DEVICES_INFO").document(
        FireStoreAuthUtil.getUserUID())
    val shoppingCreditLineCollRef = curentUserDocRef.collection(SHOPPING_CREDIT_LINE)

    val airtimeCreditLineCollRef = curentUserDocRef.collection(AIRTIME_CREDIT_LINE)

    val chatChanelCollRef = firestoreInstance.collection(CHAT_CHANNEL)
    val creditLineComLoanCollRef = curentUserDocRef.collection(CREDIT_LINE_COMMUNITY_LOAN)
    val campaignComLoanCollRef = firestoreInstance.collection(CAMPAIGN_COMMUNITY_LOAN)
    val fraudsterCollection = firestoreInstance.collection(FRAUDERS)
    val storeCollRef = firestoreInstance.collection(STORES_COLL_NAME)


}
