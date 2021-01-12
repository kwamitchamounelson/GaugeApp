package com.example.gaugeapp.dataSource.communityLoan.campaign.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.entities.communityLoan.CampaignComLoan
import com.example.gaugeapp.utils.FirebaseResponseType
import com.example.gaugeapp.utils.printLogD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

private const val TAG = "CampaignComLoanRemoteSe"

/**
 * Campaign com loan remote service
 *
 * @property firestoreInstance
 * @constructor Create empty Campaign com loan remote service
 */
@ExperimentalCoroutinesApi
class CampaignComLoanRemoteService @Inject constructor(
    private val firestoreInstance: FirebaseFirestore
) {

    /**
     * Create campaign com loan
     *
     * @param campaignComLoan
     * @return
     */
    fun createCampaignComLoan(campaignComLoan: CampaignComLoan): Flow<FirebaseResponseType<CampaignComLoan>> =
        callbackFlow {
            try {
                val task = FireStoreCollDocRef.campaignComLoanCollRef.add(campaignComLoan)
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
                                val data = value.toObject(CampaignComLoan::class.java)
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
     * Update campaign com loan
     *
     * @param campaignComLoan
     * @return
     */
    fun UpdateCampaignComLoan(campaignComLoan: CampaignComLoan): Flow<FirebaseResponseType<CampaignComLoan>> =
        callbackFlow {
            try {
                val task = FireStoreCollDocRef.campaignComLoanCollRef
                    .document(campaignComLoan.id)
                    .set(campaignComLoan, SetOptions.merge())

                task
                    .addOnSuccessListener {
                        try {
                            offer(FirebaseResponseType.FirebaseSuccessResponse(campaignComLoan))
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
     * Get all campaign com loan
     *
     * @return
     */
    fun getAllCampaignComLoan(): Flow<FirebaseResponseType<List<CampaignComLoan>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.campaignComLoanCollRef
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(CampaignComLoan::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                printLogD(TAG, ex.toString())
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Get all active campaign com loan
     *
     * @return
     */
    fun getAllActiveCampaignComLoan(): Flow<FirebaseResponseType<List<CampaignComLoan>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.campaignComLoanCollRef
                    .whereEqualTo(CampaignComLoan::enable.name, true)
                    .orderBy(CampaignComLoan::createAt.name, Query.Direction.ASCENDING)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(CampaignComLoan::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                printLogD(TAG, ex.toString())
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Get campaign start at syn date real time
     *
     * @param syncDate
     * @return
     */
    fun getCampaignStartAtSynDateRealTime(syncDate: Date): Flow<FirebaseResponseType<List<CampaignComLoan>>> =
        callbackFlow {
            try {
                val snapshot = FireStoreCollDocRef.campaignComLoanCollRef
                    .orderBy(CampaignComLoan::createAt.name, Query.Direction.ASCENDING)
                    .startAt(syncDate)

                snapshot.addSnapshotListener { value, error ->
                    if (error != null) {
                        try {
                            offer(FirebaseResponseType.FirebaseErrorResponse(error))
                        } catch (ex: Exception) {
                            printLogD(TAG, ex.toString())
                        }
                    } else {
                        try {
                            val data = value?.map { queryDocumentSnapshot ->
                                val entity =
                                    queryDocumentSnapshot.toObject(CampaignComLoan::class.java)
                                entity.apply {
                                    id = queryDocumentSnapshot.id
                                }
                            }
                            offer(FirebaseResponseType.FirebaseSuccessResponse(data))
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
     * Get all un solved campaign of user
     *
     * @param userId
     * @return
     */
    fun getAllUnSolvedCampaignOfUser(userId: String): Flow<FirebaseResponseType<List<CampaignComLoan>>> =
        flow {
            try {
                val snapshot = FireStoreCollDocRef.campaignComLoanCollRef
                    .whereEqualTo(CampaignComLoan::solved.name, true)
                    .whereEqualTo(CampaignComLoan::ownerId.name, userId)
                    .get()
                    .await()

                val data = snapshot.map { queryDocumentSnapshot ->
                    val entity = queryDocumentSnapshot.toObject(CampaignComLoan::class.java)
                    entity.apply {
                        id = queryDocumentSnapshot.id
                    }
                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(data))
            } catch (ex: Exception) {
                printLogD(TAG, ex.toString())
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }
}