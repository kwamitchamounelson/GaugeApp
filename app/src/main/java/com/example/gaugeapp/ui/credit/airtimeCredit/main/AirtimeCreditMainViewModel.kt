package com.example.gaugeapp.ui.credit.airtimeCredit.main

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.entities.AirTimeCreditLine
import com.example.gaugeapp.repositories.creditRepositories.AirtimeCreditRepository
import com.example.gaugeapp.ui.base.BaseViewModel
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Airtime credit main view model
 *
 * @property repository
 * @constructor
 *
 * @param application
 * @param savedStateHandle
 */
@InternalCoroutinesApi
class AirtimeCreditMainViewModel @ViewModelInject constructor(
    private val repository: AirtimeCreditRepository,
    application: Application,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    /**
     * current User id
     */
    //val userId = FireStoreAuthUtil.getUserUID()
    val userId = "FireStoreAuthUtil.getUserUID()"

    /**
     * Current airtime credit line list observer
     *
     * to display current credit line and here credits
     */
    val currentAirtimeCreditLinetObserver = MutableLiveData<DataState<AirTimeCreditLine>>()

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
     * @param airtimeCreditStateEvent
     */
    fun setStateEvent(airtimeCreditStateEvent: AirtimeCreditStateEvent) {
        val job = viewModelScope.launch {
            when (airtimeCreditStateEvent) {
                is AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser -> {
                    repository.getCurrentCreditLineOfTheUser(userId).onEach {
                        currentAirtimeCreditLinetObserver.value = it
                    }.launchIn(viewModelScope)
                }
                is AirtimeCreditStateEvent.BorrowAirtimeCredit -> {
                    repository.borrowAirTimeCredit(
                        userId,
                        airtimeCreditStateEvent.creditLineId,
                        airtimeCreditStateEvent.amount,
                        airtimeCreditStateEvent.phoneNumber
                    ).onEach {
                        //TODO success borrow
                    }.launchIn(viewModelScope)
                }
                is AirtimeCreditStateEvent.InitAirtimeCreditLine -> {
                    repository.createCreditLine(airtimeCreditStateEvent.airtimeCredit).onEach {
                        //TODO create new Airtime Credit line
                    }.launchIn(viewModelScope)
                }
            }
        }
        jobList["AirtimeCreditMain"] = job
    }

    /**
     * Calculate credit left of the current credit line
     *
     * @param airTimeCreditLine
     * @return
     */
    fun calculateCreditLeft(airTimeCreditLine: AirTimeCreditLine): Double {
        val totalDueNotPercentage = airTimeCreditLine.airtimeCreditList.filter {
            !it.solved
        }.sumByDouble {
            it.amount
        }
        return (airTimeCreditLine.maxAmountToLoan - totalDueNotPercentage)
    }


    /**
     * Calculate credit due of the current credit line
     *
     * @param airTimeCreditLine
     * @return
     */
    fun calculateCreditDue(airTimeCreditLine: AirTimeCreditLine): Double {
        val totalDueNotPercentage = airTimeCreditLine.airtimeCreditList.filter {
            !it.solved
        }.sumByDouble {
            it.amount
        }
        return (totalDueNotPercentage * (1 + airTimeCreditLine.payBackPercent))
    }

}