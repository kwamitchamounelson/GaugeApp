package com.example.gaugeapp.ui.credit.shoppingCredit.parteneredStores

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.gaugeapp.entities.Store
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
    application: Application,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {


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

}