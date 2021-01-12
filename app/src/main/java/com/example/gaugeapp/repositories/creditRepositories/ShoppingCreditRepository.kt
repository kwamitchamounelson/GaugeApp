package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.entities.ShoppingCredit
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.entities.ShoppingCreditLineWithShoppingCreditsList
import com.example.gaugeapp.dataSource.credit.shoppingCredit.local.IShoppingCreditLocalDataSource
import com.example.gaugeapp.dataSource.credit.shoppingCredit.remote.IShoppingCreditRemoteDataSource
import com.example.gaugeapp.utils.DataState
import com.example.gaugeapp.utils.getOneDayInMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class ShoppingCreditRepository @Inject constructor(
    private val shoppingCreditLocalDataSource: IShoppingCreditLocalDataSource,
    private val shoppingCreditRemoteDataSource: IShoppingCreditRemoteDataSource
) {
    fun borrowShoppingCreditTimeCredit(
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

    fun getCurrentCreditLineOfTheUser(userUId: String): Flow<DataState<ShoppingCreditLineWithShoppingCreditsList>> {
        //Mock data
        return flow {
            val creditLineId = "ooeuubdbbvdbd"

            val creditList = arrayListOf<ShoppingCredit>().apply {
                (1..10).forEach {
                    add(
                        ShoppingCredit().apply {
                            uid = "$it"
                            userUid = userUId
                            idCreditLine = creditLineId
                            amount = listOf(500.0, 1000.0, 2500.0).random()
                        }
                    )
                }
            }

            val nowDateMillis = Calendar.getInstance().timeInMillis
            val withinAweek = Date((getOneDayInMillis() * 90) + nowDateMillis)
            val yesterday = Date((nowDateMillis - getOneDayInMillis()))
            val dueDateRandom = listOf(withinAweek, yesterday).random()

            val shoppingCreditLine = ShoppingCreditLine().apply {
                uid = creditLineId
                userId = userUId
                dueDate = dueDateRandom
            }

            val mockData =
                ShoppingCreditLineWithShoppingCreditsList(shoppingCreditLine, creditList)
            emit(DataState.Success(mockData))
        }
    }

    fun createCreditLine(shoppingCredit: ShoppingCredit): Flow<DataState<ShoppingCreditLineWithShoppingCreditsList>> {
        return flow {
            emit(
                DataState.Success(
                    ShoppingCreditLineWithShoppingCreditsList(
                        ShoppingCreditLine(),
                        listOf()
                    )
                )
            )
        }
    }

    fun getAllSolvedCreditLineOfTheUser(userUId: String): Flow<DataState<List<ShoppingCreditLineWithShoppingCreditsList>>> {
        //Mock data
        return flow {
            val list = arrayListOf<ShoppingCreditLineWithShoppingCreditsList>()

            val nowDateMillis = Calendar.getInstance().timeInMillis
            val withinAWeek = Date((getOneDayInMillis() * 90) + nowDateMillis)
            val otherDay = Date((getOneDayInMillis() * ((1..100).random())) + nowDateMillis)
            val yesterday = Date((nowDateMillis - getOneDayInMillis()))
            val dueDateRandom = listOf(withinAWeek, yesterday).random()



            (1..10).forEach { creditLineId ->
                val creditList = arrayListOf<ShoppingCredit>().apply {
                    (1..10).forEach {
                        add(
                            ShoppingCredit().apply {
                                uid = "$it"
                                userUid = userUId
                                idCreditLine = creditLineId.toString()
                                amount = listOf(500.0, 1000.0, 2500.0).random()
                                _isSolved = true
                                repaymentDate = listOf(withinAWeek, yesterday, otherDay).random()
                            }
                        )
                    }
                }

                val shoppingCreditLine = ShoppingCreditLine().apply {
                    uid = creditLineId.toString()
                    userId = userUId
                    dueDate = dueDateRandom
                }

                val mockData =
                    ShoppingCreditLineWithShoppingCreditsList(shoppingCreditLine, creditList)
                list.add(mockData)
            }

            emit(DataState.Success(list))
        }
    }
}