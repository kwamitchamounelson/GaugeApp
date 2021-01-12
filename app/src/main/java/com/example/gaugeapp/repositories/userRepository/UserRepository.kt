package com.example.gaugeapp.repositories.userRepository

import com.kola.kola_entities_features.entities.KUser
import com.example.gaugeapp.dataSource.user.local.UserLocalDataSource
import com.example.gaugeapp.dataSource.user.remote.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {

    fun getTheUsersTheUserHaveInContact (listPhoneNumber: List<String>) : Flow<List<KUser>> = flow {
        /*
        TODO: - get the list of phone number
         - look at the local database to see the the User already there
         - for all the other phone numbers check it on the server and update the list of user
         */

        remoteDataSource.getUserWithListOfPhoneNumber(listPhoneNumber).collect {

        }
    }
}