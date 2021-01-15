package com.example.gaugeapp.dataSource

import com.example.gaugeapp.utils.FirebaseResponseType
import com.example.gaugeapp.utils.cLog
import com.example.gaugeapp.utils.printLogD
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Firebase create  update and delete service
 *
 * @property TAG
 * @constructor Create empty Firebase c u d service
 *
 * @author Tsafack Dagha C.
 */
@ExperimentalCoroutinesApi
abstract class FirebaseCUDService(val TAG: String) {

    /**
     * Save data
     *
     * Save all type of Data on firebase Firestore
     *
     * @param T type of data
     * @param data data to save
     * @param collRef collection reference of data
     * @param TAG tag of called function
     * @return Flow<FirebaseResponseType<T>>
     */
    fun <T> saveData(
        data: T,
        collRef: CollectionReference
    ): Flow<FirebaseResponseType<T>> =
        callbackFlow {

            try {
                val task = collRef
                    .document()
                    .set(data!!, SetOptions.merge())
                //in case of success we
                task.addOnSuccessListener {
                    try {
                        offer(FirebaseResponseType.FirebaseSuccessResponse(data))
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

            cancelFlow(this)
        }


    /**
     * Update data
     *
     * to update data on firebase Firestore
     *
     * @param T type of data
     * @param idOfDocument id of document to update
     * @param data updated data
     * @param collRef collection of document to update
     * @param TAG tag of called function
     * @return Flow<FirebaseResponseType<T>>
     */
    fun <T> updateData(
        idOfDocument: String,
        data: T,
        collRef: CollectionReference
    ): Flow<FirebaseResponseType<T>> =
        callbackFlow {

            try {
                val task = collRef
                    .document(idOfDocument)
                    .set(data!!, SetOptions.merge())
                //in case of success we
                task.addOnSuccessListener {
                    try {
                        offer(FirebaseResponseType.FirebaseSuccessResponse(data))
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

            cancelFlow(this)
        }

    /**
     * Deleted data
     *
     * to delete data on Firebase firestore
     * @param T type of data to delete
     * @param idOfDocument id of document to delete
     * @param collRef collection of document to delete
     * @param TAG tag of called function
     * @return Flow<FirebaseResponseType<Boolean>>
     */
    fun deletedData(
        idOfDocument: String,
        collRef: CollectionReference
    ): Flow<FirebaseResponseType<Boolean>> =
        callbackFlow {

            try {
                val task = collRef
                    .document(idOfDocument)
                    .delete()
                //in case of success we
                task.addOnSuccessListener {
                    try {
                        offer(FirebaseResponseType.FirebaseSuccessResponse(true))
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
            cancelFlow(this)
        }


    /**
     * Cancel flow
     * To stop listing on current flow
     * @param producerScope
     */
    protected suspend fun <T> cancelFlow(
        producerScope: ProducerScope<FirebaseResponseType<T>>
    ) {
        try {
            producerScope.awaitClose {
                producerScope.cancel()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            cLog(ex.message)
            printLogD(TAG, ex.message ?: "")
        }
    }

    protected suspend fun <T> cancelFlowCache(
        producerScope: ProducerScope<T>
    ) {
        try {
            producerScope.awaitClose {
                producerScope.cancel()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            cLog(ex.message)
            printLogD(TAG, ex.message ?: "")
        }
    }
}