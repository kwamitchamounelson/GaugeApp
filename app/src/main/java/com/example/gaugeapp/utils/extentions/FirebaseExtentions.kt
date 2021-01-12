package com.example.gaugeapp.utils.extentions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.example.gaugeapp.utils.printLogD
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

private const val TAG = "FirebaseExtentions"

/**
 * @author Tsafack Dagha c.
 * This is an extention file for firebase generics queries
 * for documents and collections
 * @resource: https://stackoverflow.com/questions/55459961/firebase-realtime-snapshot-listener-using-coroutines
 */


/**
 * @author tsafix
 * Get query snapshot flow
 * Generic function to get specific collection realTime
 * with listener registrations
 * @return flow<QuerySnapshot>
 */
@ExperimentalCoroutinesApi
fun CollectionReference.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
    return callbackFlow {
        val listenerRegistration =
            addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    cancel(
                        message = "error fetching collection data at path - $path",
                        cause = firebaseFirestoreException
                    )
                    return@addSnapshotListener
                }
                offer(querySnapshot)
            }
        awaitClose {
            printLogD(TAG, "cancelling the listener on collection at path - $path")
            listenerRegistration.remove()
        }
    }
}


/**
 * @author tsafix
 * Get query snapshot flow
 * Generic function to get specific document realTime
 * with listener registration
 * @return flow<DocumentSnapshot>
 */
@ExperimentalCoroutinesApi
fun DocumentReference.getQuerySnapshotFlow(): Flow<DocumentSnapshot?> {
    return callbackFlow {
        val listenerRegistration =
            addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    cancel(
                        message = "error fetching collection data at path - $path",
                        cause = firebaseFirestoreException
                    )
                    return@addSnapshotListener
                }
                offer(documentSnapshot)
            }
        awaitClose {
            printLogD(TAG, "cancelling the listener on collection at path - $path")
            listenerRegistration.remove()
        }
    }
}

/**
 * Get data flow
 * @author tsafix
 * this function permit to get data from collection as flow in realtime
 * with query snapshot
 * @param T : type of elements
 * @param mapper :the  mapper to convert QuerySnapshot to type T
 * @return Flow<T>
 */
@ExperimentalCoroutinesApi
fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
    return getQuerySnapshotFlow()
        .map {
            return@map mapper(it)
        }
}


/**
 * Get data flow
 * @author tsafix
 * this function permit to get data from document as flow in realtime
 * with query snapshot
 * @param T : type of elements
 * @param mapper :the  mapper to convert DocumentSnapshot to type T
 * @return Flow<T>
 */
@ExperimentalCoroutinesApi
fun <T> DocumentReference.getDataFlow(mapper: (DocumentSnapshot?) -> T): Flow<T> {
    return getQuerySnapshotFlow()
        .map {
            return@map mapper(it)
        }
}


/**
 * @author tsafix
 * USAGE EXAMPLE:
 *
 * 1: getDataFlow FOR COLLECTION
@ExperimentalCoroutinesApi
fun getShoppingListItemsFlow(): Flow<List<ShoppingListItem>> {
    return FirebaseFirestore.getInstance()
        .collection("$COLLECTION_SHOPPING_LIST")
        .getDataFlow { querySnapshot ->
    querySnapshot?.documents?.map {
        getShoppingListItemFromSnapshot(it)
        } ?: listOf()
    }
}

// Parses the document snapshot to the desired object
    fun getShoppingListItemFromSnapshot(documentSnapshot: DocumentSnapshot) : ShoppingListItem {
        return documentSnapshot.toObject(ShoppingListItem::class.java)!!
    }

 * 2: getDataFlow FOR DOCUMENT
@ExperimentalCoroutinesApi
fun getShoppingItemFlow(): Flow<ShoppingListItem?> {
    return FirebaseFirestore.getInstance()
        .collection("$COLLECTION_SHOPPING_LIST")
        .document("$SHOPPING_ITEM_ID")
        .getDataFlow { documentSnapshot ->
            getShoppingItemFromSnapshot(it)
        }
}

// Parses the document snapshot to the desired object
fun getShoppingItemFromSnapshot(documentSnapshot: DocumentSnapshot) : ShoppingListItem {
    return documentSnapshot.toObject(ShoppingListItem::class.java)!!
}
 */