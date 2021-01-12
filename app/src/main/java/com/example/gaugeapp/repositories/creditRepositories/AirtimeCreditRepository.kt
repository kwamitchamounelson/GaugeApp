package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.local.AirtimeCreditLineLocalDataSource
import com.example.gaugeapp.dataSource.credit.AirtimeCreditLine.remote.AirTimeCreditLineRemoteDataSource
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.entities.AirtimeCredit
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.getOneDayInMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class AirtimeCreditRepository @Inject constructor(
    private val airtimeCreditLineLocalDataSource: AirtimeCreditLineLocalDataSource,
    private val airTimeCreditLineRemoteDataSource: AirTimeCreditLineRemoteDataSource
) {
    fun getCurrentCreditLineOfTheUser(useruid: String): Flow<DataState<AirTimeCreditLine>> {
        //Mock data
        return flow {
            val creditLineId = "ooeuubdbbvdbd"

            val creditList = arrayListOf<AirtimeCredit>().apply {
                (1..4).forEach {
                    add(
                        AirtimeCredit().apply {
                            id = "$it"
                            idCreditLine = creditLineId
                            amount = 100.0
                        }
                    )
                }
            }

            val nowDateMillis = Calendar.getInstance().timeInMillis
            val withinAweek = Date((getOneDayInMillis() * 7) + nowDateMillis)
            val yesterday = Date((nowDateMillis - getOneDayInMillis()))
            val dueDateRandom = listOf(withinAweek, yesterday).random()

            val mockData = AirTimeCreditLine().apply {
                id = creditLineId
                userId = useruid
                airtimeCreditList = creditList
                dueDate = dueDateRandom
            }
            emit(DataState.Success(mockData))
        }
    }

    fun borrowAirTimeCredit(
        userId: String,
        creditLineId: String,
        amount: Double,
        phoneNumber: String
    ): Flow<DataState<Boolean>> {
        //Mock data
        return flow {
            emit(DataState.Success(true))
        }
    }

    fun createCreditLine(airtimeCredit: AirtimeCredit): Flow<DataState<AirTimeCreditLine>> {
        //Mock data
        val airTimeCreditLine = AirTimeCreditLine().apply {
            airtimeCreditList = listOf(airtimeCredit)
        }
        return flow {
            emit(DataState.Success(airTimeCreditLine))
        }
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


}