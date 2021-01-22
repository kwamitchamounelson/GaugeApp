package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.ShoppingCreditLineLocalDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.ShoppingCreditLineRemoteDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCreditLineRequest.remote.ShoppingCreditRequestRemoteDataSource
import com.example.gaugeapp.entities.*
import com.example.gaugeapp.ui.credit.ConstantCredit
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.getOneDayInMillis
import com.example.gaugeapp.utils.networkBoundResource.networkBoundResourceCreditLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ShoppingCreditRepository @Inject constructor(
    private val shoppingCreditLineLocalDataSource: ShoppingCreditLineLocalDataSource,
    private val shoppingCreditLineRemoteDataSource: ShoppingCreditLineRemoteDataSource,
    private val shoppingCreditRequestRemoteDataSource: ShoppingCreditRequestRemoteDataSource
) {


    private val LIMIT_REMOTE: Long = 5

    /**
     * Get current credit line of the user
     *
     * @return
     */
    fun getCurrentShoppingCreditLineRealTime(): Flow<DataState<ShoppingCreditLine?>> {
        return shoppingCreditLineRemoteDataSource.getCurrentShoppingCreditLineRealTime()
    }


    fun getCurrentShoppingCreditLineNotFlowRealTime(onComplete: (DataState<ShoppingCreditLine?>) -> Unit) {
        shoppingCreditLineRemoteDataSource.getCurrentShoppingCreditLineNotFlowRealTime(onComplete)
    }


    /**
     * Request borrow shopping credit
     *
     * @param shoppingCreditRequest
     */
    fun requestBorrowShoppingCredit(shoppingCreditRequest: ShoppingCreditRequest) {
        shoppingCreditRequestRemoteDataSource.createShoppingCreditRequest(shoppingCreditRequest)
    }

    /**
     * Create credit line
     *
     */
    fun createCreditLine() {
        val nowDateMillis = Calendar.getInstance().timeInMillis
        val shoppingCreditLine = ShoppingCreditLine().apply {
            userId = FireStoreAuthUtil.getUserUID()
            maxAmountToLoan = ConstantCredit.MAX_AMOUNT_TO_LOAN_SHOPPING
            dueDate = Date((getOneDayInMillis() * ConstantCredit.MAX_DUE_DAY_COUNT) + nowDateMillis)
            shoppingCreditList = listOf()
            payBackPercent = ConstantCredit.INTEREST_RATE
            minAmountToLoan = ConstantCredit.MIN_AMOUNT_TO_LOAN_SHOPPING
            createAt = Date(nowDateMillis)
            syncDate = Date(nowDateMillis)
            solved = false
        }
        shoppingCreditLineRemoteDataSource.createShoppingCreditLine(shoppingCreditLine)
    }


    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    @InternalCoroutinesApi
    fun getAllSolvedCreditLineOfTheUserFirsTime(): Flow<DataState<List<ShoppingCreditLine>>> {

        return networkBoundResourceCreditLine(
            fetchFromLocal = {
                shoppingCreditLineLocalDataSource.getAllSolvedCreditLineOfTheUser()
            },
            shouldFetchFromRemote = { localList ->
                localList?.isEmpty()
                    ?: true
            },
            fetchFromRemote = {
                shoppingCreditLineRemoteDataSource.getAllSolvedCreditLineOfTheUser()
            },
            processRemoteResponse = { dataState ->
                dataState.extractData ?: arrayListOf()
            },
            saveRemoteData = { remoteList ->
                GlobalScope.launch {
                    shoppingCreditLineLocalDataSource.insertManyShoppingCreditLine(remoteList)
                }
            }, onFetchFailed = { _: String?, _: Int ->

            }
        ).flowOn(Dispatchers.IO)

    }


    /**
     * Get all solved credit line of the user after date
     *
     * @param lastCreditLine
     * @return
     */
    @InternalCoroutinesApi
    fun getAllSolvedCreditLineOfTheUserAfterDate(lastCreditLine: ShoppingCreditLine): Flow<DataState<List<ShoppingCreditLine>>> {

        return networkBoundResourceCreditLine(
            fetchFromLocal = {
                flow {
                    emit(listOf<ShoppingCreditLine>())
                }
            },
            shouldFetchFromRemote = {
                true
            },
            fetchFromRemote = {
                shoppingCreditLineRemoteDataSource.getAllSolvedCreditLineOfTheUserStartAfter(
                    lastCreditLine.createAt
                )
            },
            processRemoteResponse = { dataState ->
                dataState.extractData ?: arrayListOf()
            },
            saveRemoteData = { remoteList ->
                GlobalScope.launch {
                    shoppingCreditLineLocalDataSource.insertManyShoppingCreditLine(remoteList)
                }
            }, onFetchFailed = { _: String?, _: Int ->

            }
        ).flowOn(Dispatchers.IO)
    }


    /**
     * Get last shopping credit request real time
     *
     * @param currentCreditLineId
     * @return
     */
    fun getLastShoppingCreditRequestRealTime(currentCreditLineId: String): Flow<DataState<ShoppingCreditRequest?>> {
        return shoppingCreditRequestRemoteDataSource.getLastShoppingCreditRequestRealTime(
            currentCreditLineId
        )
    }


    fun getLastShoppingCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<ShoppingCreditRequest?>) -> Unit
    ) {
        shoppingCreditRequestRemoteDataSource.getLastShoppingCreditRequestNotFlowRealTime(
            currentCreditLineId,
            onComplete
        )
    }


    /**
     * Close validated shopping credit request
     *
     * @param currentShoppingCreditLine
     * @param currentShoppingCreditRequest
     */
    fun closeValidatedShoppingCreditRequest(
        currentShoppingCreditLine: ShoppingCreditLine,
        currentShoppingCreditRequest: ShoppingCreditRequest
    ) {

        val nowDate = Calendar.getInstance().time
        val dueDateFromNow =
            Date((getOneDayInMillis() * ConstantCredit.MAX_DUE_DAY_COUNT) + nowDate.time)


        //we disable current airtime credit request
        currentShoppingCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
        }
        shoppingCreditRequestRemoteDataSource.updateShoppingCreditRequest(
            currentShoppingCreditRequest,
            onSuccess = {
                //we add a loan to the current credit line
                val currentLoanList = arrayListOf<ShoppingCredit>().apply {
                    addAll(currentShoppingCreditLine.shoppingCreditList)
                }
                val newShoppingCredit = ShoppingCredit().apply {
                    id = nowDate.time.toString()
                    idCreditLine = currentShoppingCreditLine.id
                    amount = currentShoppingCreditRequest.amount
                    solved = false
                    dateOfLoan = nowDate
                    repaymentDate = currentShoppingCreditLine.dueDate
                }
                currentShoppingCreditLine.apply {
                    if (currentLoanList.isEmpty()) {
                        dueDate = dueDateFromNow
                        createAt = nowDate
                        syncDate = nowDate
                        newShoppingCredit.repaymentDate = dueDateFromNow
                    }
                    currentLoanList.add(newShoppingCredit)

                    shoppingCreditList = currentLoanList
                }

                shoppingCreditLineRemoteDataSource.updateShoppingCreditLine(
                    currentShoppingCreditLine
                )
            },
            onError = {

            }
        )
    }


    /**
     * Close shopping credit request
     *
     * @param currentShoppingCreditRequest
     */
    fun closeShoppingCreditRequest(currentShoppingCreditRequest: ShoppingCreditRequest) {
        //we disable current airtime credit request
        disableShoppingCreditRequest(currentShoppingCreditRequest)
    }


    /**
     * Disable shopping credit request
     *
     * @param currentShoppingCreditRequest
     */
    private fun disableShoppingCreditRequest(currentShoppingCreditRequest: ShoppingCreditRequest) {
        val nowDate = Calendar.getInstance().time
        currentShoppingCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
        }
        shoppingCreditRequestRemoteDataSource.updateShoppingCreditRequest(
            currentShoppingCreditRequest
        )
    }


    /**
     * Cancel closel shopping credit request
     *
     * @param currentShoppingCreditRequest
     */
    fun cancelCloselShoppingCreditRequest(currentShoppingCreditRequest: ShoppingCreditRequest) {
        val nowDate = Calendar.getInstance().time
        currentShoppingCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
            status = ENUM_REQUEST_STATUS.CANCELED
        }
        shoppingCreditRequestRemoteDataSource.updateShoppingCreditRequest(
            currentShoppingCreditRequest
        )
    }


    /**
     * Close current credit line
     *
     * @param currentShoppingCreditLine
     */
    fun closeCurrentCreditLine(currentShoppingCreditLine: ShoppingCreditLine) {
        val nowDate = Calendar.getInstance().time
        currentShoppingCreditLine.apply {
            solved = true
            syncDate = nowDate
        }
        shoppingCreditLineRemoteDataSource.updateShoppingCreditLine(currentShoppingCreditLine,
            onSuccess = {
                GlobalScope.launch {
                    shoppingCreditLineLocalDataSource.insertShoppingCreditLine(
                        currentShoppingCreditLine
                    )
                }
            },
            onError = {

            }
        )
    }


    /**
     * Update credit line
     *
     * @param shoppingCreditLine
     */
    fun updateCreditLine(shoppingCreditLine: ShoppingCreditLine) {
        shoppingCreditLineRemoteDataSource.updateShoppingCreditLine(shoppingCreditLine)
    }


}