package com.example.gaugeapp.dataSource.user.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.kola.kola_entities_features.entities.KUser
import com.example.gaugeapp.utils.printLogD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    UserRemoteDataSource {

    val TAG = "UserRemoteDataSourceImpl"
    override fun getUserWithListOfPhoneNumber (listPhoneNumber: List<String>): Flow<List<KUser>> = flow {
        val listUser = mutableListOf<KUser>()
        val listIds = mutableListOf<String>()

        listPhoneNumber.forEachIndexed { index, s ->
            listIds.add(s)
            if ( (index + 1) %10 == 0) {
                val list = firestore.collection(USER_COLLECTION_REF)
                    .whereIn(PHONE_NUMBER_FIELD, listIds)
                    .get()
                    .await()
                    .toObjects(KUser::class.java)
                listUser.addAll(list)
                listIds.clear()
            }
        }

        printLogD(TAG, "list user: $listUser")
        emit(listUser)
    }

    companion object {
        val USER_COLLECTION_REF = "KUSERS"
        val PHONE_NUMBER_FIELD = "authPhoneNumber"
    }
}