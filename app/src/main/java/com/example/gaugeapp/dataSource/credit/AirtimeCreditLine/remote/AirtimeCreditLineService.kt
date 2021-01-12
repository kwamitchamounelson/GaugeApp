package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.utils.FirebaseResponseType
import com.example.gaugeapp.utils.printLogD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "AirtimeCreditLineServic"

/**
 * Airtime credit line service
 *
 * @property firestoreInstance
 * @constructor Create empty Airtime credit line service
 */
@ExperimentalCoroutinesApi
class AirtimeCreditLineService @Inject constructor(
    private val firestoreInstance: FirebaseFirestore
) {


    /**
     * Create airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    fun createAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine): Flow<FirebaseResponseType<AirTimeCreditLine>> =
        callbackFlow {
            try {
                val task = FireStoreCollDocRef.airtimeCreditLineCollRef.add(airtimeCreditLine)
                task.result.addSnapshotListener { value, error ->
                    if (error != null) {
                        try {
                            offer(FirebaseResponseType.FirebaseErrorResponse(error))
                        } catch (ex: Exception) {
                            printLogD(TAG, ex.toString())
                        }
                    } else {
                        try {
                            if (value != null) {
                                val data = value.toObject(AirTimeCreditLine::class.java)
                                data?.id = value.id
                                offer(FirebaseResponseType.FirebaseSuccessResponse(data))
                            }
                        } catch (ex: Exception) {
                            printLogD(TAG, ex.toString())
                        }
                    }
                }
            } catch (ex: Exception) {
                printLogD(TAG, ex.toString())
                offer(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    fun UpdateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine): Flow<FirebaseResponseType<AirTimeCreditLine>> =
        callbackFlow {
            try {
                val task = FireStoreCollDocRef.airtimeCreditLineCollRef
                    .document(airtimeCreditLine.id)
                    .set(airtimeCreditLine, SetOptions.merge())

                task
                    .addOnSuccessListener {
                        try {
                            offer(FirebaseResponseType.FirebaseSuccessResponse(airtimeCreditLine))
                        } catch (ex: Exception) {
                            printLogD(TAG, ex.toString())
                        }
                    }
                    .addOnFailureListener { exception ->
                        try {
                            offer(FirebaseResponseType.FirebaseErrorResponse(exception))
                        } catch (ex: Exception) {
                            printLogD(TAG, ex.toString())
                        }
                    }
            } catch (ex: Exception) {
                printLogD(TAG, ex.toString())
                offer(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Get all airtime credit line
     *
     * @return
     */
    fun getAllAirtimeCreditLine(): Flow<FirebaseResponseType<List<AirTimeCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.airtimeCreditLineCollRef
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(AirTimeCreditLine::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Get current airtime credit line of the user
     *
     * @return
     */
    fun getCurrentAirtimeCreditLine(): Flow<FirebaseResponseType<List<AirTimeCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.airtimeCreditLineCollRef
                    .whereEqualTo(AirTimeCreditLine::solved.name, false)
                    .orderBy(AirTimeCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(AirTimeCreditLine::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


}