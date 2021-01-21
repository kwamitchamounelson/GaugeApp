package com.example.gaugeapp.ui.credit.airtimeCredit.main

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.entities.AirtimeCreditRequest
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
    val userId = FireStoreAuthUtil.getUserUID()

    /**
     * Current airtime credit line list observer
     *
     * to display current credit line and here credits
     */
    val currentAirtimeCreditLinetObserver = MutableLiveData<DataState<AirTimeCreditLine?>>()


    val airtimeCreditRequestObserver = MutableLiveData<DataState<AirtimeCreditRequest?>>()

    /**
     * Set state event
     *
     *
     * @param airtimeCreditStateEvent
     */
    fun setStateEvent(airtimeCreditStateEvent: AirtimeCreditStateEvent) {
        val job = viewModelScope.launch {
            when (airtimeCreditStateEvent) {
                is AirtimeCreditStateEvent.GetCurrentAirtimeCreditLineOfTheUser -> {
                    /*repository.getCurrentAirtimeCreditLineRealTime().onEach {
                        currentAirtimeCreditLinetObserver.value = it
                    }.launchIn(viewModelScope)*/

                    repository.getCurrentAirtimeCreditLineNotFlowRealTime {
                        currentAirtimeCreditLinetObserver.value = it
                    }
                }
                is AirtimeCreditStateEvent.RequestBorrowAirtimeCredit -> {
                    repository.requestBorrowAirTimeCredit(airtimeCreditStateEvent.airtimeCreditRequest)
                }
                is AirtimeCreditStateEvent.InitAirtimeCreditLine -> {
                    repository.createCreditLine()
                }
                is AirtimeCreditStateEvent.GetLastAirtimeCreditRequest -> {
                    /*repository.getLastAirtimeCreditRequestRealTime(airtimeCreditStateEvent.currentCreditLineId)
                        .onEach {
                            airtimeCreditRequestObserver.value = it
                        }.launchIn(viewModelScope)*/

                    repository.getLastAirtimeCreditRequestNotFlowRealTime(airtimeCreditStateEvent.currentCreditLineId) {
                        airtimeCreditRequestObserver.value = it
                    }
                }
                is AirtimeCreditStateEvent.CloseValidatedAirtimeCreditRequest -> {
                    repository.closeValidatedAirtimeCreditRequest(
                        airtimeCreditStateEvent.currentAirtimeCreditLine,
                        airtimeCreditStateEvent.currentAirtimeCreditRequest
                    )
                }
                is AirtimeCreditStateEvent.CloseAirtimeCreditRequest -> {
                    repository.closeAirtimeCreditRequest(airtimeCreditStateEvent.currentAirtimeCreditRequest)
                }
                is AirtimeCreditStateEvent.CancelCloselAirtimeCreditRequest -> {
                    repository.cancelCloselAirtimeCreditRequest(airtimeCreditStateEvent.currentAirtimeCreditRequest)
                }
                is AirtimeCreditStateEvent.CloseCurentCreditLine -> {
                    repository.closeCurrentCreditLine(airtimeCreditStateEvent.currentAirtimeCreditLine)
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
        val totalDueNotPercentage = airTimeCreditLine.airtimeCreditList.sumByDouble {
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