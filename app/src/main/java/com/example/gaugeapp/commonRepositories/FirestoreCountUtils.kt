package com.example.gaugeapp.commonRepositories

import android.content.Context
import android.util.Log
import com.example.gaugeapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.example.gaugeapp.data.entities.Count
import com.example.gaugeapp.utils.userPrefUtils.UserSharedPref

/**
 * this class contain all the functions to access to the firebase database to perform actions
 * about budgets in the application.
 * The collection which contain budget in the database is related to the user and is the subcollection of the collection of users called KUSERS.
 * It is like that because budget is not a share between users.
 * And this collection does not contain Cashflows because cashflows are independent from budget.
 * @author Yvana
 */
object FirestoreCountUtils {

    val TAG = "FIRESTORE"
    private lateinit var userpref: UserSharedPref
    private val colors = listOf<Int>(
        R.color.color_budget_1,
        R.color.color_budget_2,
        R.color.color_budget_3,
        R.color.color_budget_4,
        R.color.color_budget_5
    )

    /**
     * this function permit to add a budget in the database
     * @author Yvana
     * @param budget is the budget the user want to store
     * @param onSuccess the function executed when the request is a success
     * @param onError the function executed when the request failed
     */
    fun addCount(budget: Count, onSuccess: () -> Unit, onError: () -> Unit) {
        Log.e(TAG, "create $budget")
        FirebaseFirestore.getInstance().collection("COUNT").document(budget.functionId).set(budget)
            .addOnSuccessListener {
                Log.d(TAG, "add a count succesfully")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG, "add a count failed " + it.message)
                onError()
            }
    }

    fun getCount(functionID: Count, onSuccess: (Count) -> Unit, onError: () -> Unit) {
        Log.e(TAG, "get count " + functionID.toString())
        FirebaseFirestore.getInstance().collection("COUNT").document(functionID.functionId)
            .get().addOnSuccessListener {
                if (!it.exists()) {
                    Log.e(TAG, "count created" + functionID.toString())
                    addCount(functionID, onSuccess = {
                        onSuccess(functionID)
                    }, onError = {})
                } else {
                    Log.e(TAG, "count existing" + functionID.toString())
                    it.toObject(Count::class.java)?.let {
                        onSuccess(it)
                    }
                }

            }.addOnFailureListener {

            }


    }


    /**
     * This function permit to update a budget.
     * We can update everything in a casflow.
     * When we update it the data is kept but if the budget are full and you want to change on into
     * another you must delete the budget and create the new one.
     * @author Yvana
     * @param budget the budget we want to update
     * @param onSuccess the function executed when the request is a success
     * @param onError the function executed when the request failed
     */
    fun updateCount(budget: Count, onSuccess: (Count) -> Unit, onError: () -> Unit) {
        FirebaseFirestore.getInstance().collection("COUNT").document(budget.functionId).set(budget)
            .addOnSuccessListener {
                Log.d(TAG, "update a budget successfully")
                onSuccess(budget)
            }
            .addOnFailureListener {
                Log.e(TAG, "update a budget failed")
                onError()
            }
    }

    /**
     * this is the function which permit to delete a budget
     * @author Yvana
     * @param budget the budget we want to delete
     * @param onSuccess the function executed when the request is a success
     * @param onError the function executed when the request failed
     */
    fun deleteCount(budget: Count, onSuccess: () -> Unit, onError: () -> Unit) {
        FirebaseFirestore.getInstance().collection("COUNT").document(budget.functionId).set(budget)
            .addOnSuccessListener {
                Log.d(TAG, "delete a budget successfully")
                onSuccess()
            }
            .addOnFailureListener {
                Log.e(TAG, "delete a budget failed")
                onError()
            }
    }


    /**
     * this function permit to get all the budgets (max 5)
     * @author Yvana
     * @param context a valide Context
     * @param onListen the function executed when the request is a success and this function return the list of budget get from the server
     * @param onError the function executed when the request failed
     */
    fun getAllBudgets(
        context: Context,
        onListen: (List<Count>) -> Unit,
        onError: () -> Unit
    ): ListenerRegistration {
        return FirebaseFirestore.getInstance().collection("COUNT")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e(TAG, "error get budgets ${firebaseFirestoreException.printStackTrace()}")
                    onError()
                    return@addSnapshotListener
                }


                querySnapshot?.documents?.forEach {

                }


            }
    }
}