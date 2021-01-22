package com.example.gaugeapp.dataSource.credit.shoppingCredit.remote

import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.dataSource.FirebaseCUDService
import com.example.gaugeapp.entities.ShoppingCreditLine
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
import java.util.*
import javax.inject.Inject

private const val TAG = "ShoppingCreditLineServi"

/**
 * Shopping credit line service
 *
 * @property firestoreInstance
 * @constructor Create empty Shopping credit line service
 */
@ExperimentalCoroutinesApi
class ShoppingCreditLineService @Inject constructor(
    private val firestoreInstance: FirebaseFirestore
) : FirebaseCUDService(TAG) {


    /**
     * Create shopping credit line
     *
     * @param shoppingCreditLine
     */
    fun createShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) {
        FireStoreCollDocRef.shoppingCreditLineCollRef
            .document()
            .set(shoppingCreditLine, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success createShoppingCreditLine")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }


    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     */
    fun updateShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine) {
        FireStoreCollDocRef.shoppingCreditLineCollRef
            .document(shoppingCreditLine.id)
            .set(shoppingCreditLine, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateShoppingCreditLine")
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
            }
    }


    /**
     * Update shopping credit line
     *
     * @param shoppingCreditLine
     * @param onSuccess
     * @param onError
     * @receiver
     * @receiver
     */
    fun updateShoppingCreditLine(
        shoppingCreditLine: ShoppingCreditLine,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        FireStoreCollDocRef.shoppingCreditLineCollRef
            .document(shoppingCreditLine.id)
            .set(shoppingCreditLine, SetOptions.merge())
            .addOnSuccessListener {
                printLogD(TAG, "Success updateShoppingCreditLine")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                printLogD(TAG, exception.toString())
                onError()
            }
    }

    /**
     * Get all shopping credit line
     *
     * @return
     */
    fun getAllShoppingCreditLine(): Flow<FirebaseResponseType<List<ShoppingCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.shoppingCreditLineCollRef
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
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
     * Get current shopping credit line
     *
     * @return
     */
    fun getCurrentShoppingCreditLine(): Flow<FirebaseResponseType<ShoppingCreditLine?>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.shoppingCreditLineCollRef
                    .whereEqualTo(ShoppingCreditLine::solved.name, false)
                    .orderBy(ShoppingCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
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
     * Get current shopping credit line real time
     *
     * @return
     */
    fun getCurrentShoppingCreditLineRealTime(): Flow<FirebaseResponseType<ShoppingCreditLine?>> =
        callbackFlow {

            try {
                FireStoreCollDocRef.shoppingCreditLineCollRef
                    .whereEqualTo(ShoppingCreditLine::solved.name, false)
                    .orderBy(ShoppingCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { value, error ->
                        try {
                            if (error != null) {
                                error.printStackTrace()
                                printLogD(TAG, error.toString())
                                offer(FirebaseResponseType.FirebaseErrorResponse(error))
                                return@addSnapshotListener
                            }
                            val data = value?.map { queryDocumentSnapshot ->
                                val entity =
                                    queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
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


    /**
     * Get current shopping credit line not flow real time
     *
     * @param onComplete
     * @receiver
     */
    fun getCurrentShoppingCreditLineNotFlowRealTime(onComplete: (DataState<ShoppingCreditLine?>) -> Unit) {

        FireStoreCollDocRef.shoppingCreditLineCollRef
            .whereEqualTo(ShoppingCreditLine::solved.name, false)
            .orderBy(ShoppingCreditLine::createAt.name, Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    printLogD(TAG, error.toString())
                    onComplete(DataState.Failure(error))
                    return@addSnapshotListener
                }
                val data = value?.map { queryDocumentSnapshot ->
                    val entity =
                        queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                val creditLine = if (data!!.isEmpty()) {
                    null
                } else {
                    data.first()
                }
                onComplete(DataState.Success(creditLine))
            }
    }

    fun getAllSolvedCreditLineOfTheUser(): Flow<FirebaseResponseType<List<ShoppingCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.shoppingCreditLineCollRef
                    .whereEqualTo(ShoppingCreditLine::solved.name, true)
                    .orderBy(ShoppingCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                try {
                    emit(FirebaseResponseType.FirebaseErrorResponse(ex))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    fun getAllSolvedCreditLineOfTheUserStartAfterLimit(
        creationDate: Date,
        limit: Long
    ): Flow<FirebaseResponseType<List<ShoppingCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.shoppingCreditLineCollRef
                    .whereEqualTo(ShoppingCreditLine::solved.name, true)
                    .orderBy(ShoppingCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .startAfter(creationDate)
                    .limit(limit)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                try {
                    emit(FirebaseResponseType.FirebaseErrorResponse(ex))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    fun getAllSolvedCreditLineOfTheUserStartAfter(creationDate: Date): Flow<FirebaseResponseType<List<ShoppingCreditLine>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.shoppingCreditLineCollRef
                    .whereEqualTo(ShoppingCreditLine::solved.name, true)
                    .orderBy(ShoppingCreditLine::createAt.name, Query.Direction.DESCENDING)
                    .startAfter(creationDate)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(ShoppingCreditLine::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                try {
                    emit(FirebaseResponseType.FirebaseErrorResponse(ex))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


}