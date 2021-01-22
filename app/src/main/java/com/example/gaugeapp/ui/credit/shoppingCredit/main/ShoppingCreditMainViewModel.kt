package com.example.gaugeapp.ui.credit.shoppingCredit.main

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.entities.ShoppingCreditLine
import com.example.gaugeapp.repositories.creditRepositories.ShoppingCreditRepository
import com.example.gaugeapp.ui.base.BaseViewModel
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@InternalCoroutinesApi
class ShoppingCreditMainViewModel @ViewModelInject constructor(
    private val repository: ShoppingCreditRepository,
    application: Application,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    /**
     * current User id
     */
    val userId = FireStoreAuthUtil.getUserUID()

    /**
     * Current Shopping credit line list observer
     *
     * to display current credit line and here credits
     */
    val currentShoppingCreditLinetObserver = MutableLiveData<DataState<ShoppingCreditLine?>>()


    val shoppingCreditRequestObserver = MutableLiveData<DataState<ShoppingCreditRequest?>>()

    /**
     * Set state event
     *
     * @param shoppingCreditStateEvent
     */
    fun setStateEvent(shoppingCreditStateEvent: ShoppingCreditStateEvent) {
        val job = viewModelScope.launch {
            when (shoppingCreditStateEvent) {
                is ShoppingCreditStateEvent.GetCurrentShoppingCreditLineOfTheUser -> {
                    repository.getCurrentShoppingCreditLineNotFlowRealTime {
                        currentShoppingCreditLinetObserver.value = it
                    }
                }
                is ShoppingCreditStateEvent.RequestBorrowShoppingCredit -> {
                    repository.requestBorrowShoppingCredit(shoppingCreditStateEvent.shoppingCreditRequest)
                }
                is ShoppingCreditStateEvent.InitShoppingCreditLine -> {
                    repository.createCreditLine()
                }
                is ShoppingCreditStateEvent.GetLastShoppingCreditRequest -> {
                    repository.getLastShoppingCreditRequestNotFlowRealTime(shoppingCreditStateEvent.currentCreditLineId) {
                        shoppingCreditRequestObserver.value = it
                    }
                }
                is ShoppingCreditStateEvent.CloseValidatedShoppingCreditRequest -> {
                    repository.closeValidatedShoppingCreditRequest(
                        shoppingCreditStateEvent.currentShoppingCreditLine,
                        shoppingCreditStateEvent.currentShoppingCreditRequest
                    )
                }
                is ShoppingCreditStateEvent.CloseShoppingCreditRequest -> {
                    repository.closeShoppingCreditRequest(shoppingCreditStateEvent.currentShoppingCreditRequest)
                }
                is ShoppingCreditStateEvent.CancelCloselShoppingCreditRequest -> {
                    repository.cancelCloselShoppingCreditRequest(shoppingCreditStateEvent.currentShoppingCreditRequest)
                }
                is ShoppingCreditStateEvent.CloseCurentCreditLine -> {
                    repository.closeCurrentCreditLine(shoppingCreditStateEvent.currentShoppingCreditLine)
                }
            }
        }
        jobList["ShoppingCreditMain"] = job
    }

    /**
     * Calculate credit left
     *
     * @param shoppingCreditLine
     * @return
     */
    fun calculateCreditLeft(shoppingCreditLine: ShoppingCreditLine): Double {
        val totalDueNotPercentage = shoppingCreditLine.shoppingCreditList.sumByDouble {
            it.amount
        }
        return (shoppingCreditLine.maxAmountToLoan - totalDueNotPercentage)
    }


    /**
     * Calculate credit due
     *
     * @param shoppingCreditLine
     * @return
     */
    fun calculateCreditDue(shoppingCreditLine: ShoppingCreditLine): Double {
        val totalDueNotPercentage = shoppingCreditLine.shoppingCreditList.filter {
            !it.solved
        }.sumByDouble {
            it.amount
        }
        return (totalDueNotPercentage * (1 + shoppingCreditLine.payBackPercent))
    }

}