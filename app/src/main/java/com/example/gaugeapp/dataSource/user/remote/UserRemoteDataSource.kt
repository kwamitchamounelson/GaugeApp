package com.example.gaugeapp.dataSource.user.remote

import com.kola.kola_entities_features.entities.KUser
import kotlinx.coroutines.flow.Flow

interface UserRemoteDataSource {

    fun getUserWithListOfPhoneNumber (listPhoneNumber: List<String>): Flow<List<KUser>>
}