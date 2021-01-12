package com.example.gaugeapp.repositories.authRepositories

import com.kola.kola_entities_features.entities.KUser
import com.kola.kola_entities_features.entities.NumberForOperator
import com.example.gaugeapp.data.entities.UserDeviceInfo
import com.example.gaugeapp.dataSource.authentification.remote.AuthRemoteDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author Tsafix
 * Auth repository
 * using to make interactions with data sources
 * just use for the authentification process
 *
 * @property authRemoteDataSourceImpl
 * @constructor Create empty Auth repository
 */
@ExperimentalCoroutinesApi
class AuthRepository @Inject constructor(
    private val authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
) {

    private val TAG = "AuthRepository"

    /**
     * Update user phone number list
     *
     * @param opList list of object type of NumberForOperator for the user
     */
    fun upDateuserPhoneNumberList(opList: ArrayList<NumberForOperator>) =
        authRemoteDataSourceImpl.upDateuserPhoneNumberList(opList)


    /**
     * Check associated phone
     * find the auth phone number assiacted to phoneNumber on another profils
     * @param phoneNumber
     */
    fun checkAssociatedPhone(phoneNumber: String) =
        authRemoteDataSourceImpl.checkAssociatedPhone(phoneNumber)


    /**
     * Save user to fire store
     * to save user on server
     * @param user : user that we want to save
     */
    fun saveUserToFireStore(user: KUser) = authRemoteDataSourceImpl.saveUserToFireStore(user)

    /**
     * Update user
     *
     * @param updatedUser
     */
    fun updateUser(updatedUser: KUser) = authRemoteDataSourceImpl.updateUser(updatedUser)

    /**
     * Get current user from firestore
     * @return Flow<DataState<KUser>>
     */
    fun getCurentUserFromFiresTore() = authRemoteDataSourceImpl.getCurentUserFromFiresTore()


    /**
     * Save latest update of user
     * use to save the latest update
     * of the application maked by the user
     * @param currentAppVersionName
     * @param currentAppVersionCode
     */
    fun saveLatestUpdateOfUser(currentAppVersionName: String, currentAppVersionCode: Long) =
        authRemoteDataSourceImpl.saveLatestUpdateOfUser(
            currentAppVersionName,
            currentAppVersionCode
        )

    /**
     * Save user device config to firebase
     * @author tsafix
     * @param deviceInfo
     */
    fun saveUserDeviceConfig(deviceInfo: UserDeviceInfo) =
        authRemoteDataSourceImpl.saveUserDeviceConfig(deviceInfo)

    /**
     * Get user auth phone number
     *
     */
    fun getUserAuthPhoneNumber() = authRemoteDataSourceImpl.getUserPhoneNumber()

    fun getCurentUserFromFiresToreOnTime() =
        authRemoteDataSourceImpl.getCurentUserFromFiresToreOnTime()

    /**
     * Update current user profile img
     *
     * @param imageUrl
     */
    fun updateCurrentUserProfileImg(imageUrl: String) =
        authRemoteDataSourceImpl.updateCurrentUserProfileImg(imageUrl)


    /**
     * Get user from firestore by phone number
     *
     * @param userPhone
     */
    fun getUserFromFiresToreByPhoneNumer(userPhone: String) =
        authRemoteDataSourceImpl.getUserFromFiresToreByPhoneNumer(userPhone)
}