package com.example.gaugeapp.dataSource.communityLoan.creditLine.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.entities.communityLoan.CreditLineComLoan
import com.example.gaugeapp.utils.FirebaseResponseType
import com.example.gaugeapp.utils.printLogD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "CreditLineComLoanRemote"

/**
 * Credit line community loan remote service
 *
 * @property firestoreInstance
 * @constructor Create empty Credit line com loan remote service
 */
@ExperimentalCoroutinesApi
class CreditLineComLoanRemoteService @Inject constructor(
    private val firestoreInstance: FirebaseFirestore
) {

    /**
     * Create credit line com loan
     *
     * @param creditLineComLoan
     * @return
     */
    fun createCreditLineComLoan(creditLineComLoan: CreditLineComLoan): Flow<FirebaseResponseType<CreditLineComLoan>> =
        callbackFlow {
            try {
                val task = FireStoreCollDocRef.creditLineComLoanCollRef.add(creditLineComLoan)
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
                                val data = value.toObject(CreditLineComLoan::class.java)
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
     * Update credit line com loan
     *
     * @param creditLineComLoan
     * @return
     */
    fun UpdateCreditLineComLoan(creditLineComLoan: CreditLineComLoan): Flow<FirebaseResponseType<CreditLineComLoan>> =
        callbackFlow {
            try {
                val task = FireStoreCollDocRef.creditLineComLoanCollRef
                    .document(creditLineComLoan.id)
                    .set(creditLineComLoan, SetOptions.merge())

                task
                    .addOnSuccessListener {
                        try {
                            offer(FirebaseResponseType.FirebaseSuccessResponse(creditLineComLoan))
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
     * Get all credit line com loan
     *
     * @return
     */
    fun getAllCreditLineComLoan(): Flow<FirebaseResponseType<List<CreditLineComLoan>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.creditLineComLoanCollRef
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(CreditLineComLoan::class.java)
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
     * Get 1current credit line com loan
     * returns the active credit line the user is on
     * @return
     */
    fun getCurrentCreditLineComLoan(): Flow<FirebaseResponseType<List<CreditLineComLoan>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.creditLineComLoanCollRef
                    .whereEqualTo(CreditLineComLoan::solved.name, false)
                    .orderBy(CreditLineComLoan::createDate.name, Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(CreditLineComLoan::class.java)
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