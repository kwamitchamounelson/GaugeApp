package com.example.gaugeapp.dataSource.authentification.remote

import com.google.firebase.auth.FirebaseAuth
import com.kola.kola_entities_features.entities.KUser
import com.kola.kola_entities_features.entities.NumberForOperator
import com.example.gaugeapp.data.entities.UserDeviceInfo
import com.example.gaugeapp.dataSource.common.BaseRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi

import javax.inject.Inject

/**
 * Auth remote data source impl
 *@author Tsafack Dagha Cedrick
 * @property firebaseAuthInstance:FirebaseAuth
 * @property service:AuthFirestoreService
 * @constructor Create empty Auth remote data source impl
 */
@ExperimentalCoroutinesApi
class AuthRemoteDataSourceImpl @Inject constructor(
    private val firebaseAuthInstance: FirebaseAuth,
    private val service: AuthFirestoreService
) : BaseRemoteDataSource(), IAuthRemoteDataSource {


    private val TAG = "AuthRemoteDataSource"

    /**
     * Get current user uid
     * The uid genereted by firebase
     */
    override fun getCurrentUserUID() = firebaseAuthInstance.currentUser?.uid
        ?: throw NullPointerException("Uid number is null..")

    /**
     * Update user phone number list
     *
     * @param opList list of NumberForOperator(number, operator) of user
     */
    override fun upDateuserPhoneNumberList(opList: ArrayList<NumberForOperator>) =
        getResult { service.upDateUserPhoneNumberList(opList) }


    /**
     * Get user phone number
     * To get the auth phone number of user
     * @return
     */
    override fun getUserPhoneNumber(): String {
        return try {
            firebaseAuthInstance.currentUser?.phoneNumber!!
        } catch (e: NullPointerException) {
            e.printStackTrace()
            ""
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
    override fun checkAssociatedPhone(phoneNumber: String) =
        getResult { service.checkAssociatedPhone(phoneNumber) }


    /**
     * Save user to fire store
     * to save user on server
     * @param user : user that we want to save
     */
    override fun saveUserToFireStore(user: KUser) = getResult { service.saveUserToFireStore(user) }

    /**
     * Update user
     * @author tsafix
     * @param updatedUser
     */
    override fun updateUser(updatedUser: KUser) = getResult { service.updateUser(updatedUser) }

    /**
     * Get current user from firestore at real time
     * @author tsafix
     * @return Flow<FirebaseResponseType<KUser>>
     */
    override fun getCurentUserFromFiresTore() = getResult { service.getCurentUserFromFiresTore() }

    /**
     * Save latest update of user
     * @author tsafix
     * update version code, version name and LatestUpdateTime of user
     * @param versionName
     * @param versionCode
     */
    override fun saveLatestUpdateOfUser(
        currentAppVersionName: String,
        currentAppVersionCode: Long
    ) = service.saveLatestUpdateOfUser(currentAppVersionName, currentAppVersionCode)

    /**
     * Save user device config to firebase
     * @author tsafix
     * @param deviceInfo
     */
    override fun saveUserDeviceConfig(deviceInfo: UserDeviceInfo) =
        service.saveUserDeviceConfig(deviceInfo)

    /**
     * Get current user from firestore on time
     *@author tsafix
     * @return Flow<FirebaseResponseType<KUser>>
     */
    override fun getCurentUserFromFiresToreOnTime() = getResult {  service.getCurentUserFromFiresToreOnTime()}

    /**
     * Update current user profile img
     *
     * @param imageUrl
     */
    override fun updateCurrentUserProfileImg(imageUrl:String) = getResult { service.updateCurentUserProfileImg(imageUrl) }

    /**
     * Get user from firestore by phone number
     *
     * @param userProfil
     * @return
     */
    override fun getUserFromFiresToreByPhoneNumer(userProfil: String) = getResult {
        service.getUserFromFiresToreByPhoneNumer(userProfil)
    }
}