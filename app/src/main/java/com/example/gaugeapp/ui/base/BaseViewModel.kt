package com.example.gaugeapp.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application
) : AndroidViewModel(application),
    CoroutineScope {

    private val job = Job()

    protected val jobList = mutableMapOf<String, Job>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        jobList["baseJob"] = job
    }

    override fun onCleared() {
        super.onCleared()
        jobList.onEach { currentJobMap ->
            currentJobMap.value.cancel()
        }
    }


}