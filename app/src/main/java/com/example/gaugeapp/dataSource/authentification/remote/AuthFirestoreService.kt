package com.example.gaugeapp.dataSource.authentification.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kola.kola_entities_features.entities.KUser
import com.kola.kola_entities_features.entities.NumberForOperator
import com.example.gaugeapp.KolaWhalletApplication
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef.CurrentUserDeviceInfoColRef
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef.curentUserDocRef
import com.example.gaugeapp.data.entities.UserDeviceInfo
import com.example.gaugeapp.data.enums.ENUMOPERATEUR
import com.example.gaugeapp.utils.*
import com.example.gaugeapp.utils.extentions.getDataFlow
import com.example.gaugeapp.utils.extentions.saveUserImageProfileInCash
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * @author Tsafack Dagha C.(tsafix)
 * Auth firestore service:
 * This class permit to do all query that link authentification and
 * firebase
 *
 * @property firebaseAuthInstance
 * @property firebaseFirestoreInstance
 * @property authDataSource
 * @constructor Create empty Auth firestore service
 */
@ExperimentalCoroutinesApi
class AuthFirestoreService @Inject constructor(
    private val firebaseAuthInstance: FirebaseAuth,
    private val firebaseFirestoreInstance: FirebaseFirestore
    // private val authDataSource: AuthRemoteDataSource
) {

    private val TAG = "AuthFirestoreService"

    fun upDateUserPhoneNumberList(
        opList: ArrayList<NumberForOperator>
    ): Flow<FirebaseResponseType<List<NumberForOperator>>> = flow {
        try {
            val userFieldMap = mutableMapOf<String, Any>()
            userFieldMap["mobileMoneyNumbers"] = opList

            /*    firebaseFirestoreInstance
                    .collection(commonFireStoreRefKeyWord.USERS_COLLECTION_REF)
                    .document(authDataSource.getCurrentUserUID())*/
            curentUserDocRef
                .set(userFieldMap, SetOptions.merge())
                .await()

            emit(FirebaseResponseType.FirebaseSuccessResponse(opList))
        } catch (ex: Exception) {
            cLog(ex.message)
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
        }
    }.catch {
        cLog(it.message)
        emit(FirebaseResponseType.FirebaseErrorResponse(it))
    }

    fun getUserFromFiresToreByAnyPhoneNumber(userPhoneNumber: String): Flow<FirebaseResponseType<KUser>> =
        flow {
            try {
                val result =
                    firebaseFirestoreInstance.collection(commonFireStoreRefKeyWord.USERS_COLLECTION_REF)
                        .whereArrayContains(
                            "phoneNumbers",
                            PhoneNumberUtils.remove237ToPhoneNumber(userPhoneNumber)
                        )
                        .get()
                        .await()

                if (result.documents.isNotEmpty()) {
                    val doc = result.documents.first()
                    val user = (doc).toObject(KUser::class.java)
                    user?.userUid = doc.id
                    userPhoneNumber.saveUserImageProfileInCash(user?.imageUrL ?: "")
                    emit(FirebaseResponseType.FirebaseSuccessResponse(user))
                } else {
                    emit(FirebaseResponseType.FirebaseSuccessResponse(null))
                }
            } catch (ex: Exception) {
                cLog(ex.message)
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }.catch {
            cLog(it.message)
            emit(FirebaseResponseType.FirebaseErrorResponse(it))
        }

    /**
     * Save user to fire store
     *
     * @param kUser: user that we want to save
     */
    fun saveUserToFireStore(newUser: KUser): Flow<FirebaseResponseType<KUser>> = callbackFlow {

        curentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            // on initialise l'utilisateur la première fois que s'il n'existe pas encore
            if (!documentSnapshot.exists()) {
                curentUserDocRef.set(newUser)
                    .addOnSuccessListener {
                        // FireStoreExpendsUtils.addDummyDataCashFlow()
                        try {

                            offer(FirebaseResponseType.FirebaseSuccessResponse(newUser))
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            cLog(ex.message)
                            printLogD(TAG, ex.message ?: "")
                        }
                    }
            } else {
                // s'il existait déja on ne l'initialise plus
                val userData = documentSnapshot.toObject(KUser::class.java)
                printLogD(TAG, "saveUserToFireStore: User already exist data: $userData")

                try {
                    offer(FirebaseResponseType.FirebaseSuccessResponse(newUser))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    cLog(ex.message)
                    printLogD(TAG, ex.message ?: "")
                }
            }
            val user = documentSnapshot.toObject(KUser::class.java)
            printLogD(TAG, "saveUserToFireStore: User data: $user")

        }.addOnFailureListener {
            it.printStackTrace()
            cLog(it.message)
            try {
                offer(FirebaseResponseType.FirebaseErrorResponse(it))
            } catch (ex: Exception) {
                ex.printStackTrace()
                cLog(ex.message)
                printLogD(TAG, ex.message ?: "")
            }
        }

        try {
            awaitClose {
                cancel()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            cLog(ex.message)
            printLogD(TAG, ex.message ?: "")
        }

    }

    /**
     * FR:
     * Check associated phone/ Cette fonction a pour rôle de checher si l'utilisateur courant a un
     * compte avec un numéro de téléphone autre que celui qu'il utilise actuellement
     *
     * ENG:
     * This function has the role of checking if the current user has an account with a phone
     * number other than the one he is currently using
     * @param phoneNumber
     * @return Kuser: the use who is associated with phoneNumber
     */
    fun checkAssociatedPhone(phoneNumber: String): Flow<FirebaseResponseType<KUser>> = flow {

        /**
         * On crèe une requette pour vérifier si le numéro de l'utilisateur est déja un numéro associé
         * à un compte existant appartenant à un opérateur different
         * */

        /**
         * On crèe une requette pour vérifier si le numéro de l'utilisateur est déja un numéro associé
         * à un compte existant appartenant à un opérateur different
         * */

        val fieldKey = if (PhoneNumberUtils.isOrangeOperator(phoneNumber)) {
            "OMPhoneNumber"
        } else {
            "MoMoPhoneNumber"
        }
        val formatedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(phoneNumber)

        var gettingUser: KUser? = null

        try {
            val result = firebaseFirestoreInstance
                .collection(commonFireStoreRefKeyWord.ASSOCIATED_PHONE_NUMBERS)
                .whereEqualTo(fieldKey, formatedPhoneNumber)
                .limit(1)
                .get()
                .await()

            val mobileMoneyNumbers = ArrayList<NumberForOperator>()

            if (result.documents.isEmpty()){
                emit(FirebaseResponseType.FirebaseSuccessResponse(gettingUser))
            }else {


                val document = result.documents.first()
                val userName = document["userName"] as String
                val authPhoneNumber = document["authPhoneNumber"] as String
                val imageUrl = document["imageUrl"] as String
                val OMPhoneNumber = document["OMPhoneNumber"] as String
                val MoMoPhoneNumber = document["MoMoPhoneNumber"] as String

                mobileMoneyNumbers.add(
                    NumberForOperator(
                        ENUMOPERATEUR.ORANGE_MONEY.name,
                        OMPhoneNumber
                    )
                )
                mobileMoneyNumbers.add(NumberForOperator(
                    ENUMOPERATEUR.MTN_MONEY.name,
                    MoMoPhoneNumber))

                /** car s'il s'authentifie avec le numéro avec lequel il s'était authentifier avec
                 * un compte existant au préalable, on doit simplement contiuer l'authentification
                 */
                if (formatedPhoneNumber != authPhoneNumber) {
                    gettingUser = KUser()
                    gettingUser.mobileMoneyNumbers = mobileMoneyNumbers
                    gettingUser.authPhoneNumber = authPhoneNumber
                    gettingUser.userName = userName
                    gettingUser.imageUrL = imageUrl
                }

                emit(FirebaseResponseType.FirebaseSuccessResponse(gettingUser))
            }
        } catch (ex: Exception) {
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            cLog(ex.message)
            printLogD(TAG, "checkAssociatedPhone: ${ex.printStackTrace()}")
        }

    }.catch {
        emit(FirebaseResponseType.FirebaseErrorResponse(it))
        cLog(it.message)
        printLogD(TAG, "checkAssociatedPhone: ${it.printStackTrace()}")
    }

    fun updateUser(updatedUser: KUser): Flow<FirebaseResponseType<KUser>> = callbackFlow {

        val phoneNumberList = updatedUser.mobileMoneyNumbers
        var secondPheNumber = ""
        phoneNumberList.forEach {
            if (PhoneNumberUtils.remove237ToPhoneNumber(it.phoneNumber) != PhoneNumberUtils.remove237ToPhoneNumber(
                    (updatedUser.authPhoneNumber)
                )
            ) {
                secondPheNumber = it.phoneNumber
            }
        }

        val phoneList = arrayListOf(
            PhoneNumberUtils.remove237ToPhoneNumber(updatedUser.authPhoneNumber),
            PhoneNumberUtils.remove237ToPhoneNumber(secondPheNumber)
        )
        updatedUser.phoneNumbers = phoneList.distinct().filter { it.isNotEmpty() }

        curentUserDocRef.set(updatedUser, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "updateUser: User has updated succesfully")
                saveOrUpdateUserAssociatedPhoneNumbers(updatedUser)
                try {
                    offer(FirebaseResponseType.FirebaseSuccessResponse(updatedUser))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    cLog(ex.message)
                    printLogD(TAG, ex.message ?: "")
                }

            }
            .addOnFailureListener {
                printLogD(TAG, "Error to updated user" + it.cause)

                try {
                    offer(FirebaseResponseType.FirebaseErrorResponse(it))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    cLog(ex.message)
                    printLogD(TAG, ex.message ?: "")
                }
            }

        awaitClose {
            this.close()
        }
    }


    fun saveOrUpdateUserAssociatedPhoneNumbers(curenUserData: KUser?) {

        curenUserData?.let { curenUser ->
            val momoNumbFOprtor =
                getOperatorFromList(curenUser.mobileMoneyNumbers, ENUMOPERATEUR.MTN_MONEY.name)
            val omNumbFOprtor =
                getOperatorFromList(curenUser.mobileMoneyNumbers, ENUMOPERATEUR.ORANGE_MONEY.name)

            val userFieldMap = mutableMapOf<String, String>()
            userFieldMap["authPhoneNumber"] = curenUser.authPhoneNumber
            userFieldMap["OMPhoneNumber"] =
                PhoneNumberUtils.formatPhoneNumber(omNumbFOprtor?.phoneNumber ?: "")
            userFieldMap["MoMoPhoneNumber"] =
                PhoneNumberUtils.formatPhoneNumber(momoNumbFOprtor?.phoneNumber ?: "")
            userFieldMap["userName"] = curenUser.userName
            userFieldMap["imageUrl"] = curenUser.imageUrL
            userFieldMap["userUid"] = FireStoreAuthUtil.getUserUID()

            try {
                firebaseFirestoreInstance
                    .collection(commonFireStoreRefKeyWord.ASSOCIATED_PHONE_NUMBERS)
                    .document(curenUser.authPhoneNumber)
                    .set(userFieldMap, SetOptions.merge()).addOnCompleteListener {
                        printLogD(TAG, "saveOrUpdateUserAssociatedPhoneNumbers: Update completed")
                    }.addOnFailureListener {
                        it.printStackTrace()
                        printLogD(TAG, "saveOrUpdateUserAssociatedPhoneNumbers: Update comfailled")
                    }
            } catch (ex: IllegalArgumentException) {
                ex.printStackTrace()
                printLogD(TAG, "An exception occured: ${ex.message}")
            }

        }
    }

    /**
     * Get current user from firestore at real time
     * @author tsafix
     * @return Flow<FirebaseResponseType<KUser>>
     */
    fun getCurentUserFromFiresTore(): Flow<FirebaseResponseType<KUser>> {
        return curentUserDocRef.getDataFlow { documentSnapshot ->
            getUserEntityFromSnapshot(documentSnapshot)
        }
    }

    /**
     * Get user entity from snapshot
     * @author tsafix
     * @param documentSnapshot
     * @return FirebaseResponseType<KUser>
     */
    private fun getUserEntityFromSnapshot(documentSnapshot: DocumentSnapshot?): FirebaseResponseType<KUser> {
        return try {
            FirebaseResponseType.FirebaseSuccessResponse(documentSnapshot?.toObject(KUser::class.java)!!)
        } catch (ex: Exception) {
            printLogD(TAG, "getUserEntityFromSnapshot: Error to get user: ${ex.message}")
            cLog(ex.message)
            FirebaseResponseType.FirebaseErrorResponse(ex)

        }
    }


    /**
     * Save latest update of user
     * @author tsafix
     * update version code, version name and LatestUpdateTime of user
     * @param versionName
     * @param versionCode
     */
    fun saveLatestUpdateOfUser(
        versionName: String,
        versionCode: Long
    ) {

        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["versionName"] = versionName
        userFieldMap["versionCode"] = versionCode
        userFieldMap["LatestUpdateTime"] = FieldValue.serverTimestamp()

        curentUserDocRef.set(userFieldMap, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "lateds update of User has updated succesfully")
                KolaWhalletApplication.userPref.saveLadestUpdates(versionName)
            }
            .addOnFailureListener {
                printLogD(TAG, "Error to saved latets update of user" + it.cause)
            }
    }

    /**
     * Save user device config to firebase
     * @author tsafix
     * @param deviceInfo
     */
    fun saveUserDeviceConfig(deviceInfo: UserDeviceInfo) {
        CurrentUserDeviceInfoColRef.set(deviceInfo, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "User device settings has updated succesfully")
            }
            .addOnFailureListener {
                printLogD(TAG, "Error to updated User device settings " + it.cause)
            }
    }


    /**
     * Get current user from firestore on time
     *@author tsafix
     * @return Flow<FirebaseResponseType<KUser>>
     */
    fun getCurentUserFromFiresToreOnTime()
            : Flow<FirebaseResponseType<KUser>> = flow {

        val responseSnapshot = curentUserDocRef.get().await()
        try {
            val user = responseSnapshot.toObject(KUser::class.java)
            if (user != null) {
                emit(FirebaseResponseType.FirebaseSuccessResponse(user))
            } else {
                emit(FirebaseResponseType.FirebaseErrorResponse(user))
            }
        } catch (ex: Exception) {
            printLogD(TAG, "Error: utilisateur inexistant " + ex.cause)
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            cLog(ex.message)
        }
    }.catch {
        // emit(FirebaseResponseType.FirebaseErrorResponse(it))
        cLog(it.message)
    }


    fun updateCurentUserProfileImg(imageUrl: String): Flow<FirebaseResponseType<String>> =
        callbackFlow {

            try {
                val fieldsToUpdate = mutableMapOf<String, String>()
                fieldsToUpdate[KUser::imageUrL.name] = imageUrl

                curentUserDocRef.set(fieldsToUpdate, SetOptions.merge())
                    .addOnSuccessListener {
                        printLogD(TAG, "User has updated succesfully")

                        try {
                            offer(FirebaseResponseType.FirebaseSuccessResponse(imageUrl))
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            cLog(ex.message)
                            printLogD(TAG, ex.message ?: "")
                        }
                    }
                    .addOnFailureListener {
                        printLogD(TAG, "Error to updated user" + it.cause)

                        try {
                            offer(FirebaseResponseType.FirebaseErrorResponse(it))
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            cLog(ex.message)
                            printLogD(TAG, ex.message ?: "")
                        }
                    }
            } catch (ex: Exception) {
                printLogD(TAG, "Error to updated user" + ex.cause)
                try {
                    offer(FirebaseResponseType.FirebaseErrorResponse(ex))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    cLog(ex.message)
                    printLogD(TAG, ex.message ?: "")
                }
            }

            awaitClose {
                this.close()
            }

        }

    fun getUserFromFiresToreByPhoneNumer(userPhoneNumber: String): Flow<FirebaseResponseType<KUser>> =
        callbackFlow {
            firebaseFirestoreInstance.collection(commonFireStoreRefKeyWord.USERS_COLLECTION_REF)
                .whereEqualTo(
                    KUser::authPhoneNumber.name,
                    PhoneNumberUtils.formatPhoneNumber(userPhoneNumber)
                )
                .get()
                .addOnSuccessListener { documents ->

                    if (documents != null && !documents.isEmpty) {
                        val doc = documents.first()
                        var user = (doc).toObject(KUser::class.java)
                        user.userUid = doc.id

                        try {
                            offer(FirebaseResponseType.FirebaseSuccessResponse(user))
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            cLog(ex.message)
                            printLogD(TAG, ex.message ?: "")
                        }
                    } else {

                        try {
                            offer(FirebaseResponseType.FirebaseErrorResponse(null))
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            cLog(ex.message)
                            printLogD(TAG, ex.message ?: "")
                        }
                    }
                }
                .addOnFailureListener { exception ->

                    try {
                        offer(FirebaseResponseType.FirebaseErrorResponse(exception))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        cLog(ex.message)
                        printLogD(TAG, ex.message ?: "")
                    }
                }

            awaitClose {
                this.close()
            }
        }
}