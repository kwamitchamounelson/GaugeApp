package com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote

import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord
import com.example.gaugeapp.data.entities.ShoppingCreditRequest
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

private const val TAG = "ShoppingCreditRequestSer"

/**
 * Shopping credit request service
 *
 * @property firestoreInstance
 * @constructor Create empty Shopping credit request service
 */
@ExperimentalCoroutinesApi
class ShoppingCreditRequestService @Inject constructor(
    private val firestoreInstance: FirebaseFirestore
) : FirebaseCUDService(TAG) {

    /**
     * Create shopping credit request
     *
     * @param shoppingCreditRequest
     */
    fun createShoppingCreditRequest(shoppingCreditRequest: ShoppingCreditRequest) {
        val collection = getCollectionReference(shoppingCreditRequest.shoppingCreditLineId)
        collection
            .document()
            .set(shoppingCreditRequest, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success createShoppingCreditRequest")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }


    /**
     * Update shopping credit request
     *
     * @param shoppingCreditRequest
     */
    fun updateShoppingCreditRequest(shoppingCreditRequest: ShoppingCreditRequest) {
        val collection = getCollectionReference(shoppingCreditRequest.shoppingCreditLineId)
        collection.document(shoppingCreditRequest.id)
            .set(shoppingCreditRequest, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateShoppingCreditRequest")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }

    fun updateShoppingCreditRequest(
        shoppingCreditRequest: ShoppingCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val collection = getCollectionReference(shoppingCreditRequest.shoppingCreditLineId)
        collection.document(shoppingCreditRequest.id)
            .set(shoppingCreditRequest, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateShoppingCreditRequest")
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
        FireStoreCollDocRef.shoppingCreditLineCollRef.document(airtimeCreditRequestId)
            .collection(commonFireStoreRefKeyWord.SHOPPING_CREDIT_REQUEST)


    /**
     * Get last shopping credit request
     *
     * @param currentCreditLineId
     * @return
     */
    fun getLastShoppingCreditRequest(currentCreditLineId: String): Flow<FirebaseResponseType<ShoppingCreditRequest?>> =
        flow {
            try {
                val snapshot = getCollectionReference(currentCreditLineId)
                    .orderBy(ShoppingCreditRequest::creationDate.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(ShoppingCreditRequest::class.java)
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


    fun getLastShoppingCreditRequestRealTime(currentCreditLineId: String): Flow<FirebaseResponseType<ShoppingCreditRequest?>> =
        callbackFlow {

            try {
                getCollectionReference(currentCreditLineId)
                    .orderBy(ShoppingCreditRequest::creationDate.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { value, error ->
                        try {
                            if (error != null) {
                                offer(FirebaseResponseType.FirebaseErrorResponse(error))
                                return@addSnapshotListener
                            }
                            val data = value?.map { queryDocumentSnapshot ->
                                val entity =
                                    queryDocumentSnapshot.toObject(ShoppingCreditRequest::class.java)
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


    fun getLastShoppingCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<ShoppingCreditRequest?>) -> Unit
    ) {

        getCollectionReference(currentCreditLineId)
            .orderBy(ShoppingCreditRequest::creationDate.name, Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    printLogD(TAG, error.toString())
                    onComplete(DataState.Failure(error))
                    return@addSnapshotListener
                }
                val data = value?.map { queryDocumentSnapshot ->
                    val entity =
                        queryDocumentSnapshot.toObject(ShoppingCreditRequest::class.java)
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