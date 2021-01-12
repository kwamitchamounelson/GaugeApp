package com.example.gaugeapp.dataSource.credit.shoppingCredit.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef
import com.example.gaugeapp.commonRepositories.FireStoreCollDocRef.shoppingCreditLineCollRef
import com.example.gaugeapp.commonRepositories.commonFireStoreRefKeyWord.SHOPPING_CREDITS
import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.utils.FirebaseResponseType
import com.example.gaugeapp.utils.printLogD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

private const val TAG = "ShoppingCreditService"

class ShoppingCreditService @Inject constructor(
    private val firebaseAuthInstance: FirebaseAuth,
    private val firestoreInstance: FirebaseFirestore
) {

    /**
     * Create shopping credit line
     *
     * @param shoppingCreditLine
     * @return
     */
    fun createShoppingCreditLine(shoppingCreditLine: ShoppingCreditLine): Flow<FirebaseResponseType<ShoppingCreditLine>> =
        flow {
            try {
                shoppingCreditLineCollRef.document()
                    .set(shoppingCreditLine, SetOptions.merge())
                    .await()
                emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCreditLine))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }

    /**
     * Update shopping credit line
     *
     * @param updatedShoppingCreditLine
     * @return
     */
    fun updateShoppingCreditLine(updatedShoppingCreditLine: ShoppingCreditLine): Flow<FirebaseResponseType<ShoppingCreditLine>> =
        flow {
            try {
                shoppingCreditLineCollRef.document(updatedShoppingCreditLine.uid)
                    .set(updatedShoppingCreditLine, SetOptions.merge())
                    .await()
                emit(FirebaseResponseType.FirebaseSuccessResponse(updatedShoppingCreditLine))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }

    /**
     * Get all shopping credit lines of user
     *
     * @return Flow<FirebaseResponseType<List<ShoppingCreditLine>>>
     */
    fun getAllShoppingCreditLinesOfUser(): Flow<FirebaseResponseType<List<ShoppingCreditLine>>> =
        flow {
            try {
                val snapshot = shoppingCreditLineCollRef
                    .get()
                    .await()

                val shoppingCreditLines = mutableListOf<ShoppingCreditLine>()
                snapshot.documents.onEach { documentSnapShot ->
                    val shopCreditLine = documentSnapShot.toObject(ShoppingCreditLine::class.java)!!
                    shopCreditLine.uid = documentSnapShot.id
                    shoppingCreditLines.add(shopCreditLine)
                }

                emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCreditLines))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }


    /**
     * Get shop credit lines of user greater than update date
     * Permet de recupérer
     * toutes les nouvelles lignes de  credit du serveur,
     * et les lignes de  credit mises à jours
     * @param date : la date à partir de laquelle on aimerait
     * recupérer les lignes de  credit
     * @return FirebaseResponseType<List<FinanceEducationArticle>>
     */
    fun getShopCreditLinesOfUserGreaterThanUpdateDate(date: Date): Flow<FirebaseResponseType<List<ShoppingCreditLine>>> =
        flow {
            try {
                val querySnapshot = shoppingCreditLineCollRef
                    .orderBy((ShoppingCreditLine::latestUpdateAt).name, Query.Direction.ASCENDING)
                    .startAfter(date)
                    .get()
                    .await()

                val shoppingCreditLines = mutableListOf<ShoppingCreditLine>()
                querySnapshot.documents.onEach { documentSnapShot ->
                    val shopCreditLine = documentSnapShot.toObject(ShoppingCreditLine::class.java)!!
                    shopCreditLine.uid = documentSnapShot.id
                    shoppingCreditLines.add(shopCreditLine)
                }

                emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCreditLines))
            } catch (e: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(e))
            }
        }.catch {
            printLogD(TAG, it.message ?: "")
        }

    /**
     * Create shopping credit
     *
     * @param shoppingCredit
     * @return
     */
    fun createShoppingCredit(
        shoppingCreditLineUid: String,
        shoppingCredit: ShoppingCredit
    ): Flow<FirebaseResponseType<ShoppingCredit>> =
        flow {
            try {
                shoppingCreditLineCollRef.document(shoppingCreditLineUid)
                    .collection(SHOPPING_CREDITS)
                    .document()
                    .set(shoppingCredit, SetOptions.merge())
                    .await()
                emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCredit))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }.catch {
            printLogD(TAG, "error" + it.message)
        }

    /**
     * Update shopping credit
     *
     * @param updatedShoppingCredit
     * @return
     */
    fun updateShoppingCredit(
        shoppingCreditLineUid: String,
        updatedShoppingCredit: ShoppingCredit
    ): Flow<FirebaseResponseType<ShoppingCredit>> =
        flow {
            try {
                shoppingCreditLineCollRef.document(shoppingCreditLineUid)
                    .collection(SHOPPING_CREDITS)
                    .document(updatedShoppingCredit.uid)
                    .set(updatedShoppingCredit, SetOptions.merge())
                    .await()
                emit(FirebaseResponseType.FirebaseSuccessResponse(updatedShoppingCredit))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }.catch {
            printLogD(TAG, "error" + it.message)
        }


    /**
     * Get all shopping credit in credit line of user
     *
     * @param shoppingCreditLineUid
     * @return
     */
    fun getAllShoppingCreditInCreditLineOfUser(shoppingCreditLineUid: String): Flow<FirebaseResponseType<List<ShoppingCredit>>> =
        flow {
            try {
                val snapshot = shoppingCreditLineCollRef.document(shoppingCreditLineUid)
                    .collection(SHOPPING_CREDITS)
                    .get()
                    .await()

                val shoppingCreditLines = mutableListOf<ShoppingCredit>()
                snapshot.documents.onEach { documentSnapShot ->
                    val shopCredit = documentSnapShot.toObject(ShoppingCredit::class.java)!!
                    shopCredit.uid = documentSnapShot.id
                    shoppingCreditLines.add(shopCredit)
                }

                emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCreditLines))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }.catch {
            printLogD(TAG, "error" + it.message)
        }

    /**
     * Get all shopping credit of user
     *
     * @return
     */
    fun getAllShoppingCreditOfUser(): Flow<FirebaseResponseType<List<ShoppingCredit>>> = flow {
        try {
            val snapshot = firestoreInstance.collectionGroup(SHOPPING_CREDITS)
                .whereEqualTo(ShoppingCredit::userUid.name, FireStoreAuthUtil.getUserUID())
                .get()
                .await()

            val shoppingCredits = mutableListOf<ShoppingCredit>()
            snapshot.documents.onEach { documentSnapShot ->
                val shopCredit = documentSnapShot.toObject(ShoppingCredit::class.java)!!
                shopCredit.uid = documentSnapShot.id
                shoppingCredits.add(shopCredit)
            }

            emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCredits))
        } catch (ex: Exception) {
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
        }
    }.catch {
        printLogD(TAG, "error" + it.message)
    }


    /**
     * Get shop credit of user greater than update date
     * Permet de recupérer
     * toutes les nouvelles lignes de  credit du serveur,
     * et les lignes de  credit mises à jours
     * @param date : la date à partir de laquelle on aimerait
     * recupérer les lignes de  credit
     * @return FirebaseResponseType<List<FinanceEducationArticle>>
     */
    fun getShopCreditOfUserGreaterThanUpdateDate(date: Date): Flow<FirebaseResponseType<List<ShoppingCredit>>> =
        flow {
            try {
                val querySnapshot = firestoreInstance
                    .collectionGroup(SHOPPING_CREDITS)
                    .whereEqualTo(ShoppingCredit::userUid.name, FireStoreAuthUtil.getUserUID())
                    .orderBy(ShoppingCredit::syncDate.name, Query.Direction.ASCENDING)
                    .startAfter(date)
                    .get()
                    .await()

                val shoppingCredits = mutableListOf<ShoppingCredit>()
                querySnapshot.documents.onEach { documentSnapShot ->
                    val shopCredit = documentSnapShot.toObject(ShoppingCredit::class.java)!!
                    shopCredit.uid = documentSnapShot.id
                    shoppingCredits.add(shopCredit)
                }

                emit(FirebaseResponseType.FirebaseSuccessResponse(shoppingCredits))
            } catch (e: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(e))
            }
        }.catch {
            printLogD(TAG, "error" + it.message)
        }


    /**
     * Get all stores
     *
     * @return
     */
    fun getAllStores(): Flow<FirebaseResponseType<List<Store>>> = flow {
        try {
            val snapshot = FireStoreCollDocRef.storeCollRef
                .get()
                .await()

            val storeList = mutableListOf<Store>()
            snapshot.documents.onEach { documentSnapShot ->
                val store = documentSnapShot.toObject(Store::class.java)!!
                store.uid = documentSnapShot.id
                storeList.add(store)
            }

            emit(FirebaseResponseType.FirebaseSuccessResponse(storeList))
        } catch (ex: Exception) {
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
        }
    }

    /**
     * Get single store
     *
     * @param storeUid
     * @return
     */
    fun getSingleStore(storeUid: String): Flow<FirebaseResponseType<Store>> = flow {
        try {
            val documentSnapShot = FireStoreCollDocRef.storeCollRef.document(storeUid)
                .get()
                .await()

            val store = documentSnapShot.toObject(Store::class.java)!!
            store.uid = documentSnapShot.id

            emit(FirebaseResponseType.FirebaseSuccessResponse(store))
        } catch (ex: Exception) {
            emit(FirebaseResponseType.FirebaseErrorResponse(ex))
        }
    }

    /**
     * Save genereted stores
     * TODO this function will be remove on application before to go at production, now it's just used to genered tests stores
     * @param stores
     * @return
     */
    fun saveGeneretedStores(stores: List<Store>): Flow<FirebaseResponseType<Boolean>> =
        flow {
            try {
                stores.forEach { store ->

                    FireStoreCollDocRef.storeCollRef.document()
                        .set(store, SetOptions.merge())
                        .await()

                }
                emit(FirebaseResponseType.FirebaseSuccessResponse(true))
            } catch (ex: Exception) {
                emit(FirebaseResponseType.FirebaseErrorResponse(ex))
            }
        }
}