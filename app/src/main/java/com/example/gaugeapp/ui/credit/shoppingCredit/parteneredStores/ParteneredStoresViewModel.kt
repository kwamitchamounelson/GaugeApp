package com.example.gaugeapp.ui.credit.shoppingCredit.parteneredStores

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gaugeapp.commonRepositories.FireStoreAuthUtil
import com.example.gaugeapp.data.entities.ShoppingCreditRequest
import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.repositories.creditRepositories.ShoppingCreditRepository
import com.example.gaugeapp.repositories.creditRepositories.StoreRepository
import com.example.gaugeapp.ui.base.BaseViewModel
import com.example.gaugeapp.utils.DataState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ParteneredStoresViewModel @ViewModelInject constructor(
    private val repository: StoreRepository,
    private val shoppingRepository: ShoppingCreditRepository,
    application: Application,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {

    /**
     * current User id
     */
    val userId = FireStoreAuthUtil.getUserUID()


    val storeListObserver =
        MutableLiveData<DataState<List<Store>>>()


    fun getAllStores() {
        val job = viewModelScope.launch {
            repository.getAllStores().onEach {
                storeListObserver.value = it
            }.launchIn(viewModelScope)
        }
        jobList["ParteneredStores"] = job
    }

    fun requestBorrowShopping(
        shoppingCreditRequest: ShoppingCreditRequest,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        shoppingRepository.requestBorrowShoppingCredit(shoppingCreditRequest,onSuccess, onError)
    }

}