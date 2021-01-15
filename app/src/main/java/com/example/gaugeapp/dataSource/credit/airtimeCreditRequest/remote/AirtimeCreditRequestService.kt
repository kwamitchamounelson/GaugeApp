package com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote

import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.dataSource.FirebaseCUDService
import com.example.gaugeapp.utils.FirebaseResponseType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
    fun createAirtimeCreditRequest(airtimeCreditRequest: AirtimeCreditRequest): Flow<FirebaseResponseType<AirtimeCreditRequest>> {
        val collection = getCollectionReference(airtimeCreditRequest.airtimeCreditLineId)
        return saveData(airtimeCreditRequest, collection)
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

    fun updateCreditRequest(airtimeCreditRequest: AirtimeCreditRequest): Flow<FirebaseResponseType<AirtimeCreditRequest>> {
        val collection = getCollectionReference(airtimeCreditRequest.airtimeCreditLineId)
        return updateData(airtimeCreditRequest.id, airtimeCreditRequest, collection)
    }


}