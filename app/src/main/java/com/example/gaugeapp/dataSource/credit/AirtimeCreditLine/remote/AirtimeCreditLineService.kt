package com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote

import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.dataSource.FirebaseCUDService
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.utils.FirebaseResponseType
import com.example.gaugeapp.utils.printLogD
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
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
) : FirebaseCUDService(TAG) {


    /**
     * Create airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    fun createAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) {
        FireStoreCollDocRef.airtimeCreditLineCollRef
            .document()
            .set(airtimeCreditLine, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success createAirtimeCreditLine")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }


    /**
     * Update airtime credit line
     *
     * @param airtimeCreditLine
     * @return
     */
    fun updateAirtimeCreditLine(airtimeCreditLine: AirTimeCreditLine) {
        FireStoreCollDocRef.airtimeCreditLineCollRef
            .document(airtimeCreditLine.id)
            .set(airtimeCreditLine, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateAirtimeCreditLine")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
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
    fun getCurrentAirtimeCreditLine(): Flow<FirebaseResponseType<AirTimeCreditLine?>> =
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
                val creditLine = if (data.isEmpty()) {
                    null
                } else {
                    data.first()
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(creditLine))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Get current airtime credit line real time
     *
     * @return
     */
    fun getCurrentAirtimeCreditLineRealTime(): Flow<FirebaseResponseType<AirTimeCreditLine?>> =
        callbackFlow {

            try {
                FireStoreCollDocRef.airtimeCreditLineCollRef
                    .whereEqualTo(AirTimeCreditLine::solved.name, false)
                    .orderBy(AirTimeCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { value, error ->
                        try {
                            if (error != null) {
                                offer(FirebaseResponseType.FirebaseErrorResponse(error))
                                return@addSnapshotListener
                            }
                            val data = value?.map { queryDocumentSnapshot ->
                                val entity =
                                    queryDocumentSnapshot.toObject(AirTimeCreditLine::class.java)
                                entity.apply {
                                    id = queryDocumentSnapshot.id
                                }
                            }
                            val creditLine = if (data!!.isEmpty()) {
                                null
                            } else {
                                data.first()
                            }
                            offer(FirebaseResponseType.FirebaseSuccessResponse(creditLine))
                        } catch (ex: Exception) {
                            printLogD(TAG, ex.toString())
                        }
                    }
            } catch (ex: Exception) {
                printLogD(TAG, ex.toString())
                offer(FirebaseResponseType.FirebaseErrorResponse(ex))
            }

            cancelFlow(this)
        }

    fun getAllSolvedCreditLineOfTheUser(): Flow<FirebaseResponseType<List<AirTimeCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.airtimeCreditLineCollRef
                    .whereEqualTo(AirTimeCreditLine::solved.name, true)
                    .orderBy(AirTimeCreditLine::createAt.name, Query.Direction.DESCENDING)
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