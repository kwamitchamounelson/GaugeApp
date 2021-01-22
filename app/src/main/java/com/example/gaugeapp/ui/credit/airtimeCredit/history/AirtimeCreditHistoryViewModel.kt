package com.example.gaugeapp.ui.credit.airtimeCredit.history

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

@InternalCoroutinesApi
class AirtimeCreditHistoryViewModel @ViewModelInject constructor(
    private val repository: AirtimeCreditRepository,
    application: Application,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    /**
     * current User id
     */
    val userId = FireStoreAuthUtil.getUserUID()

    val listAirtimeCreditLinetFirstTimeObserver =
        MutableLiveData<DataState<List<AirTimeCreditLine>>>()


    fun setStateEvent(airtimeCreditHistoryStateEven: AirtimeCreditHistoryStateEven) {
        val job = viewModelScope.launch {
            when (airtimeCreditHistoryStateEven) {
                is AirtimeCreditHistoryStateEven.GetDataFirstTime -> {
                    repository.getAllSolvedCreditLineOfTheUserFirsTime().onEach {
                        listAirtimeCreditLinetFirstTimeObserver.value = it
                    }.launchIn(viewModelScope)
                }
                is AirtimeCreditHistoryStateEven.GetDataAfterFirstTime -> {
                    repository.getAllSolvedCreditLineOfTheUserAfterDate(
                        airtimeCreditHistoryStateEven.lastCreditLine
                    ).onEach {
                    }.launchIn(viewModelScope)
                }
            }
        }
        jobList["AirtimeCreditHistory"] = job
    }

}