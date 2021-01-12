package com.example.gaugeapp.di

import com.example.gaugeapp.repositories.common.StorageRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@EntryPoint
@InstallIn(ApplicationComponent::class)
interface RepositoryProviderEntryPoint {
    fun storageRepository(): StorageRepository
}

