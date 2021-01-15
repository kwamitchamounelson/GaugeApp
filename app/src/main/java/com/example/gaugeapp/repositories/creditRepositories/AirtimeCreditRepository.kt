package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.entities.AirtimeCreditRequest
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimeCreditLineLocalDataSource
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote.AirTimeCreditLineRemoteDataSource
import com.example.gaugeapp.dataSource.credit.airtimeCreditRequest.remote.AirTimeCreditRequestRemoteDataSource
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.entities.AirtimeCredit
import com.example.gaugeapp.ui.credit.ConstantCredit
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.getOneDayInMillis
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

@InternalCoroutinesApi
class AirtimeCreditRepository @Inject constructor(
    private val airtimeCreditLineLocalDataSource: AirtimeCreditLineLocalDataSource,
    private val airtimeCreditLineRemoteDataSource: AirTimeCreditLineRemoteDataSource,
    private val airtimeCreditRequestRemoteDataSource: AirTimeCreditRequestRemoteDataSource
) {

    fun getCurrentCreditLineOfTheUser(): Flow<DataState<AirTimeCreditLine?>> {
        return airtimeCreditLineRemoteDataSource.getCurrentAirtimeCreditLineRealTime()
    }

    fun requestBorrowAirTimeCredit(airtimeCreditRequest: AirtimeCreditRequest){
         airtimeCreditRequestRemoteDataSource.createAirtimeCreditRequest(airtimeCreditRequest)
    }

    fun createCreditLine(){
        val nowDateMillis = Calendar.getInstance().timeInMillis
        val airTimeCreditLine = AirTimeCreditLine().apply {
            userId = FireStoreAuthUtil.getUserUID()
            maxAmountToLoan = ConstantCredit.MAX_AMOUNT_TO_LOAN
            dueDate = Date((getOneDayInMillis() * ConstantCredit.MAX_DUE_DAY_COUNT) + nowDateMillis)
            airtimeCreditList = listOf()
            payBackPercent = ConstantCredit.INTEREST_RATE
            minAmountToLoan = ConstantCredit.MIN_AMOUNT_TO_LOAN
            createAt = Date(nowDateMillis)
            syncDate = Date(nowDateMillis)
            solved = false
        }
        airtimeCreditLineRemoteDataSource.createAirtimeCreditLine(airTimeCreditLine)
    }

    fun getAllSolvedCreditLineOfTheUser(userUid: String): Flow<DataState<List<AirTimeCreditLine>>> {
        //Mock data
        return flow {

            val list = arrayListOf<AirTimeCreditLine>()

            val nowDateMillis = Calendar.getInstance().timeInMillis
            val withinAWeek = Date((getOneDayInMillis() * 7) + nowDateMillis)
            val otherDay = Date((getOneDayInMillis() * ((1..100).random())) + nowDateMillis)
            val yesterday = Date((nowDateMillis - getOneDayInMillis()))

            (1..10).forEach { creditLineId ->
                val creditList = arrayListOf<AirtimeCredit>().apply {
                    (1..5).forEach {
                        add(
                            AirtimeCredit().apply {
                                id = "$it"
                                idCreditLine = creditLineId.toString()
                                amount = 100.0
                                solved = true
                                repaymentDate = listOf(withinAWeek, yesterday, otherDay).random()
                            }
                        )
                    }
                }

                val dueDateRandom = listOf(withinAWeek, yesterday, otherDay).random()
                val mockData = AirTimeCreditLine().apply {
                    id = creditLineId.toString()
                    userId = userUid
                    airtimeCreditList = creditList
                    dueDate = dueDateRandom
                    solved = true
                }
                list.add(mockData)
            }

            emit(DataState.Success(list))
        }
    }

    fun getLastRequestReal(currentCreditLineId: String): Flow<DataState<AirtimeCreditRequest?>> {
        return airtimeCreditRequestRemoteDataSource.getLastAirtimeCreditRequestRealTime(
            currentCreditLineId
        )
    }

    fun closeValidatedAirtimeCreditRequest(
        currentAirtimeCreditLine: AirTimeCreditLine,
        currentAirtimeCreditRequest: AirtimeCreditRequest
    ) {

        val nowDate = Calendar.getInstance().time
        val dueDateFromNow =
            Date((getOneDayInMillis() * ConstantCredit.MAX_DUE_DAY_COUNT) + nowDate.time)


        //we disable current airtime credit request
        disableAirtimeCreditRequest(currentAirtimeCreditRequest)

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
    }

    fun disableAirtimeCreditRequest(currentAirtimeCreditRequest: AirtimeCreditRequest) {
        val nowDate = Calendar.getInstance().time
        currentAirtimeCreditRequest.apply {
            lastUpdatedDate = nowDate
            requestEnable = false
        }
        airtimeCreditRequestRemoteDataSource.updateAirtimeCreditRequest(currentAirtimeCreditRequest)
    }


}