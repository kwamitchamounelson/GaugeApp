package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.data.enums.ENUM_REQUEST_STATUS
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimeCreditLineLocalDataSource
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote.AirTimeCreditLineRemoteDataSource
import com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote.AirTimeCreditRequestRemoteDataSource
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.entities.AirtimeCredit
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


/**
 * Airtime credit repository
 *
 * @property airtimeCreditLineLocalDataSource
 * @property airtimeCreditLineRemoteDataSource
 * @property airtimeCreditRequestRemoteDataSource
 * @constructor Create empty Airtime credit repository
 */
@InternalCoroutinesApi
class AirtimeCreditRepository @Inject constructor(
    private val airtimeCreditLineLocalDataSource: AirtimeCreditLineLocalDataSource,
    private val airtimeCreditLineRemoteDataSource: AirTimeCreditLineRemoteDataSource,
    private val airtimeCreditRequestRemoteDataSource: AirTimeCreditRequestRemoteDataSource
) {


    private val LIMIT_REMOTE: Long = 5

    /**
     * Get current credit line of the user
     *
     * @return
     */
    fun getCurrentAirtimeCreditLineRealTime(): Flow<DataState<AirTimeCreditLine?>> {
        return airtimeCreditLineRemoteDataSource.getCurrentAirtimeCreditLineRealTime()
    }


    fun getCurrentAirtimeCreditLineNotFlowRealTime(onComplete: (DataState<AirTimeCreditLine?>) -> Unit) {
        airtimeCreditLineRemoteDataSource.getCurrentAirtimeCreditLineNotFlowRealTime(onComplete)
    }


    /**
     * Request borrow air time credit
     *
     * @param airtimeCreditRequest
     */
    fun requestBorrowAirTimeCredit(airtimeCreditRequest: AirtimeCreditRequest) {
        airtimeCreditRequestRemoteDataSource.createAirtimeCreditRequest(airtimeCreditRequest)
    }

    /**
     * Create credit line
     *
     */
    fun createCreditLine() {
        val nowDateMillis = Calendar.getInstance().timeInMillis
        val airTimeCreditLine = AirTimeCreditLine().apply {
            userId = FireStoreAuthUtil.getUserUID()
            maxAmountToLoan = ConstantCredit.MAX_AMOUNT_TO_LOAN_AIRTIME
            dueDate = Date((getOneDayInMillis() * ConstantCredit.MAX_DUE_DAY_COUNT) + nowDateMillis)
            airtimeCreditList = listOf()
            payBackPercent = ConstantCredit.INTEREST_RATE
            minAmountToLoan = ConstantCredit.MIN_AMOUNT_TO_LOAN_AIRTIME
            createAt = Date(nowDateMillis)
            syncDate = Date(nowDateMillis)
            solved = false
        }
        airtimeCreditLineRemoteDataSource.createAirtimeCreditLine(airTimeCreditLine)
    }


    /**
     * Get all solved credit line of the user
     *
     * @return
     */
    @InternalCoroutinesApi
    fun getAllSolvedCreditLineOfTheUserFirsTime(): Flow<DataState<List<AirTimeCreditLine>>> {

        return networkBoundResourceCreditLine(
            fetchFromLocal = {
                airtimeCreditLineLocalDataSource.getAllSolvedCreditLineOfTheUser()
            },
            shouldFetchFromRemote = { localList ->
                localList?.isEmpty()
                    ?: true
            },
            fetchFromRemote = {
                airtimeCreditLineRemoteDataSource.getAllSolvedCreditLineOfTheUser()
            },
            processRemoteResponse = { dataState ->
                dataState.extractData ?: arrayListOf()
            },
            saveRemoteData = { remoteList ->
                GlobalScope.launch {
                    airtimeCreditLineLocalDataSource.insertManyAirtimeCreditLine(remoteList)
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
    fun getAllSolvedCreditLineOfTheUserAfterDate(lastCreditLine: AirTimeCreditLine): Flow<DataState<List<AirTimeCreditLine>>> {

        return networkBoundResourceCreditLine(
            fetchFromLocal = {
                flow {
                    emit(listOf<AirTimeCreditLine>())
                }
            },
            shouldFetchFromRemote = {
                true
            },
            fetchFromRemote = {
                airtimeCreditLineRemoteDataSource.getAllSolvedCreditLineOfTheUserStartAfter(
                    lastCreditLine.createAt
                )
            },
            processRemoteResponse = { dataState ->
                dataState.extractData ?: arrayListOf()
            },
            saveRemoteData = { remoteList ->
                GlobalScope.launch {
                    airtimeCreditLineLocalDataSource.insertManyAirtimeCreditLine(remoteList)
                }
            }, onFetchFailed = { _: String?, _: Int ->

            }
        ).flowOn(Dispatchers.IO)
    }


    /**
     * Get last request real
     *
     * @param currentCreditLineId
     * @return
     */
    fun getLastAirtimeCreditRequestRealTime(currentCreditLineId: String): Flow<DataState<AirtimeCreditRequest?>> {
        return airtimeCreditRequestRemoteDataSource.getLastAirtimeCreditRequestRealTime(
            currentCreditLineId
        )
    }


    fun getLastAirtimeCreditRequestNotFlowRealTime(
        currentCreditLineId: String,
        onComplete: (DataState<AirtimeCreditRequest?>) -> Unit
    ) {
        airtimeCreditRequestRemoteDataSource.getLastAirtimeCreditRequestNotFlowRealTime(
            currentCreditLineId,
            onComplete
        )
    }


    /**
     * Close validated airtime credit request
     *
     * @param currentAirtimeCreditLine
     * @param currentAirtimeCreditRequest
     */
    fun closeValidatedAirtimeCreditRequest(
        currentAirtimeCreditLine: AirTimeCreditLine,
        currentAirtimeCreditRequest: AirtimeCreditRequest
    ) {

        val nowDate = Calendar.getInstance().time
        val dueDateFromNow =
            Date((getOneDayInMillis() * ConstantCredit.MAX_DUE_DAY_COUNT) + nowDate.time)


        //we disable current airtime credit request
        currentAirtimeCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
        }
        airtimeCreditRequestRemoteDataSource.updateAirtimeCreditRequest(
            currentAirtimeCreditRequest,
            onSuccess = {
                //we add a loan to the current credit line
                val currentLoanList = arrayListOf<AirtimeCredit>().apply {
                    addAll(currentAirtimeCreditLine.airtimeCreditList)
                }
                val newAirtimeCredit = AirtimeCredit().apply {
                    id = nowDate.time.toString()
                    idCreditLine = currentAirtimeCreditLine.id
                    amount = currentAirtimeCreditRequest.amount
                    solved = false
                    dateOfLoan = nowDate
                    repaymentDate = currentAirtimeCreditLine.dueDate
                }
                currentAirtimeCreditLine.apply {
                    if (currentLoanList.isEmpty()) {
                        dueDate = dueDateFromNow
                        createAt = nowDate
                        syncDate = nowDate
                        newAirtimeCredit.repaymentDate = dueDateFromNow
                    }
                    currentLoanList.add(newAirtimeCredit)

                    airtimeCreditList = currentLoanList
                }

                airtimeCreditLineRemoteDataSource.updateAirtimeCreditLine(currentAirtimeCreditLine)
            },
            onError = {

            }
        )
    }


    /**
     * Close airtime credit request
     *
     * @param currentAirtimeCreditRequest
     */
    fun closeAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest) {
        //we disable current airtime credit request
        disableAirtimeCreditRequest(currentAirtimeCreditRequest)
    }


    /**
     * Disable airtime credit request
     *
     * @param currentAirtimeCreditRequest
     */
    private fun disableAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest) {
        val nowDate = Calendar.getInstance().time
        currentAirtimeCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
        }
        airtimeCreditRequestRemoteDataSource.updateAirtimeCreditRequest(currentAirtimeCreditRequest)
    }


    /**
     * Cancel closel airtime credit request
     *
     * @param currentAirtimeCreditRequest
     */
    fun cancelCloselAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest) {
        val nowDate = Calendar.getInstance().time
        currentAirtimeCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
            status = ENUM_REQUEST_STATUS.CANCELED
        }
        airtimeCreditRequestRemoteDataSource.updateAirtimeCreditRequest(currentAirtimeCreditRequest)
    }


    /**
     * Close current credit line
     *
     * @param currentAirtimeCreditLine
     */
    fun closeCurrentCreditLine(currentAirtimeCreditLine: AirTimeCreditLine) {
        val nowDate = Calendar.getInstance().time
        currentAirtimeCreditLine.apply {
            solved = true
            syncDate = nowDate
        }
        airtimeCreditLineRemoteDataSource.updateAirtimeCreditLine(currentAirtimeCreditLine,
            onSuccess = {
                GlobalScope.launch {
                    airtimeCreditLineLocalDataSource.insertAirtimeCreditLine(
                        currentAirtimeCreditLine
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
     * @param airTimeCreditLine
     */
    fun updateCreditLine(airTimeCreditLine: AirTimeCreditLine) {
        airtimeCreditLineRemoteDataSource.updateAirtimeCreditLine(airTimeCreditLine)
    }


}