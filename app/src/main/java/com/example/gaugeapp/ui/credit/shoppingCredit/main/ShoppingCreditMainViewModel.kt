package com.example.gaugeapp.ui.credit.shoppingCredit.main

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.entities.ShoppingCreditLineWithShoppingCreditsList
import com.example.gaugeapp.repositories.creditRepositories.ShoppingCreditRepository
import com.example.gaugeapp.ui.base.BaseViewModel
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    //val userId = FireStoreAuthUtil.getUserUID()
    val userId = "FireStoreAuthUtil.getUserUID()"


    /**
     * Current credit line list observer
     *
     * to display current credit line and here credits
     */
    val currentCreditLinetObserver =
        MutableLiveData<DataState<ShoppingCreditLineWithShoppingCreditsList>>()

    /**
     * State event credit observer
     *
     * To manage the state of the user
     * 0 by default if the user is in good standing and is not in the process of making a request
     * 1 if a loan request is pending
     * 2 if he is overdue
     *
     */
    val stateEventCreditObserver = MutableLiveData<Int>().apply {
        value = 0
    }

    /**
     * Set state event
     *
     * @param shoppingCreditStateEvent
     */
    fun setStateEvent(shoppingCreditStateEvent: ShoppingCreditStateEvent) {
        val job = viewModelScope.launch {
            when (shoppingCreditStateEvent) {
                is ShoppingCreditStateEvent.GetCurrentShoppingCreditLineOfTheUser -> {
                    repository.getCurrentCreditLineOfTheUser(userId).onEach {
                        currentCreditLinetObserver.value = it
                    }.launchIn(viewModelScope)
                }
                is ShoppingCreditStateEvent.BorrowShoppingCredit -> {
                    repository.borrowShoppingCreditTimeCredit(
                        userId,
                        shoppingCreditStateEvent.creditLineId,
                        shoppingCreditStateEvent.amount,
                        shoppingCreditStateEvent.phoneNumber
                    ).onEach {
                        //TODO success borrow
                    }.launchIn(viewModelScope)
                }
                is ShoppingCreditStateEvent.InitShoppingCreditLine -> {
                    repository.createCreditLine(shoppingCreditStateEvent.shoppingCredit).onEach {
                        //TODO create new Airtime Credit line
                    }.launchIn(viewModelScope)
                }
            }
        }
        jobList["ShoppingCreditMain"] = job
    }

    /**
     * Calculate credit left of the current credit line
     *
     * @param shoppingCreditLineWithShoppingCreditsList
     * @return
     */
    fun calculateCreditLeft(shoppingCreditLineWithShoppingCreditsList: ShoppingCreditLineWithShoppingCreditsList): Double {
        val totalDueNotPercentage = shoppingCreditLineWithShoppingCreditsList.creditList.filter {
            !it._isSolved
        }.sumByDouble {
            it.amount
        }
        return (shoppingCreditLineWithShoppingCreditsList.shoppingCreditLine.amount - totalDueNotPercentage)
    }


    /**
     * Calculate credit due of the current credit line
     *
     * @param shoppingCreditLineWithShoppingCreditsList
     * @return
     */
    fun calculateCreditDue(shoppingCreditLineWithShoppingCreditsList: ShoppingCreditLineWithShoppingCreditsList): Double {
        val totalDueNotPercentage = shoppingCreditLineWithShoppingCreditsList.creditList.filter {
            !it._isSolved
        }.sumByDouble {
            it.amount
        }
        return (totalDueNotPercentage * (1 + shoppingCreditLineWithShoppingCreditsList.shoppingCreditLine.payBackPercent))
    }


}