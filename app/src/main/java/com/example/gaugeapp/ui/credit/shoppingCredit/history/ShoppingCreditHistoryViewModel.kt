package com.example.gaugeapp.ui.credit.shoppingCredit.history

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
class ShoppingCreditHistoryViewModel @ViewModelInject constructor(
    private val repository: ShoppingCreditRepository,
    application: Application,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    /**
     * current User id
     */
    val userId = FireStoreAuthUtil.getUserUID()

    val listShoppingCreditLinetObserver =
        MutableLiveData<DataState<List<ShoppingCreditLineWithShoppingCreditsList>>>()

    fun getHistoryOfCredit() {
        val job = viewModelScope.launch {
            repository.getAllSolvedCreditLineOfTheUser(userId).onEach {
                listShoppingCreditLinetObserver.value = it
            }.launchIn(viewModelScope)
        }
        jobList["ShoppingCreditHistory"] = job
    }

}