package com.example.gaugeapp.dataSource.authentification.remote

import com.kola.kola_entities_features.entities.KUser
import com.kola.kola_entities_features.entities.NumberForOperator
import com.example.gaugeapp.data.entities.UserDeviceInfo
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.flow.Flow

interface IAuthRemoteDataSource {

    /**
     * @author tsafack D. Cedrick
     * Get current user uid
     * @return userUid
     */
    fun getCurrentUserUID(): String

    /**
     * Update user phone number list
     *
     * @param opList: phone numberList
     * @return numberList
     */
    fun upDateuserPhoneNumberList(opList: ArrayList<NumberForOperator>): Flow<DataState<List<NumberForOperator>>>


    fun getUserPhoneNumber(): String

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
    fun checkAssociatedPhone(phoneNumber: String): Flow<DataState<KUser>>

    /**
     * Save user to fire store
     * to save user on server
     * @param user : user that we want to save
     */
    fun saveUserToFireStore(user: KUser): Flow<DataState<KUser>>

    /**
     * Update user
     *
     * @param updatedUser
     */
    fun updateUser(updatedUser: KUser): Flow<DataState<KUser>>

    /**
     * Get current user from firestore at real time
     * @author tsafix
     * @return Flow<FirebaseResponseType<KUser>>
     */
    fun getCurentUserFromFiresTore():Flow<DataState<KUser>>


    /**
     * Save latest update of user
     * @author tsafix
     * update version code, version name and LatestUpdateTime of user
     * @param versionName
     * @param versionCode
     */
    fun saveLatestUpdateOfUser( currentAppVersionName:String,
                                currentAppVersionCode:Long)

    /**
     * Save user device config to firebase
     * @author tsafix
     * @param deviceInfo
     */
    fun saveUserDeviceConfig(deviceInfo: UserDeviceInfo)

    /**
     * Get current user from firestore on time
     *@author tsafix
     * @return Flow<FirebaseResponseType<KUser>>
     */
    fun getCurentUserFromFiresToreOnTime():Flow<DataState<KUser>>

    /**
     * Update current user profile img
     *
     * @param imageUrl
     * @return
     */
    fun updateCurrentUserProfileImg(imageUrl:String):Flow<DataState<String>>

    /**
     * Get user from firestore by phone number
     *
     * @param userProfil
     * @return
     */
    fun getUserFromFiresToreByPhoneNumer(userProfil: String):Flow<DataState<KUser>>
}