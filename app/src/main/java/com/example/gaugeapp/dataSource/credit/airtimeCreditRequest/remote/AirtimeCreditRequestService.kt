package com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote

import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.dataSource.FirebaseCUDService
import com.example.gaugeapp.utils.DataState
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

private const val TAG = "AirtimeCreditRequestSer"

/**
 * Airtime credit request service
 *
 * @property firestoreInstance
 * @constructor Create empty Airtime credit request service
 */
@ExperimentalCoroutinesApi
class AirtimeCreditRequestService @Inject constructor(
    private val firestoreInstance: FirebaseFirestore
) : FirebaseCUDService(TAG) {

    /**
     * Create airtime credit request
     *
     * @param airtimeCreditRequest
     * @return
     */
    fun createAirtimeCreditRequest(airtimeCreditRequest: AirtimeCreditRequest) {
        val collection = getCollectionReference(airtimeCreditRequest.airtimeCreditLineId)
        collection
            .document()
            .set(airtimeCreditRequest, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success createAirtimeCreditRequest")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }


    /**
     * Update airtime credit request
     *
     * @param airtimeCreditRequest
     * @return
     */
    fun updateAirtimeCreditRequest(airtimeCreditRequest: AirtimeCreditRequest) {
        val collection = getCollectionReference(airtimeCreditRequest.airtimeCreditLineId)
        collection.document(airtimeCreditRequest.id)
            .set(airtimeCreditRequest, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateAirtimeCreditRequest")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }

    fun updateAirtimeCreditRequest(
        airtimeCreditRequest: AirtimeCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val collection = getCollectionReference(airtimeCreditRequest.airtimeCreditLineId)
        collection.document(airtimeCreditRequest.id)
            .set(airtimeCreditRequest, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateAirtimeCreditRequest")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
                onError()
            }
    }

    /**
     * Get collection reference
     *
     * @param airtimeCreditRequestId
     */
    private fun getCollectionReference(airtimeCreditRequestId: String) =
        FireStoreCollDocRef.airtimeCreditLineCollRef.document(airtimeCreditRequestId)
            .collection(commonFireStoreRefKeyWord.AIRTIME_CREDIT_REQUEST)


    /**
     * Get activated airtime credit request
     *
     * @param currentCreditLineId
     * @return
     */
    fun getLastAirtimeCreditRequest(currentCreditLineId: String): Flow<FirebaseResponseType<AirtimeCreditRequest?>> =
        flow {
            try {
                val snapshot = getCollectionReference(currentCreditLineId)
                    .orderBy(AirtimeCreditRequest::creationDate.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(AirtimeCreditRequest::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                val creditLRequest = if (data.isEmpty()) {
                    null
                } else {
                    data.first()
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(creditLRequest))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    fun getLastAirtimeCreditRequestRealTime(currentCreditLineId: String): Flow<FirebaseResponseType<AirtimeCreditRequest?>> =
        callbackFlow {

            try {
                getCollectionReference(currentCreditLineId)
                    .orderBy(AirtimeCreditRequest::creationDate.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { value, error ->
                        try {
                            if (error != null) {
                                offer(FirebaseResponseType.FirebaseErrorResponse(error))
                                return@addSnapshotListener
                            }
                            val data = value?.map { queryDocumentSnapshot ->
                                val entity =
                                    queryDocumentSnapshot.toObject(AirtimeCreditRequest::class.java)
                                entity.apply {
                                    id = queryDocumentSnapshot.id
                                }
                            }
                            val response = if (data!!.isEmpty()) {
                                null
                            } else {
                                data.first()
                            }
                            offer(FirebaseResponseType.FirebaseSuccessResponse(response))
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


    fun getLastAirtimeCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<AirtimeCreditRequest?>) -> Unit
    ) {

        getCollectionReference(currentCreditLineId)
            .orderBy(AirtimeCreditRequest::creationDate.name, Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    printLogD(TAG, error.toString())
                    onComplete(DataState.Failure(error))
                    return@addSnapshotListener
                }
                val data = value?.map { queryDocumentSnapshot ->
                    val entity =
                        queryDocumentSnapshot.toObject(AirtimeCreditRequest::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                val response = if (data!!.isEmpty()) {
                    null
                } else {
                    data.first()
                }
                onComplete(DataState.Success(response))
            }

    }


}