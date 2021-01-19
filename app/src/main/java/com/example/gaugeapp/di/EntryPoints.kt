package com.example.gaugeapp.di

import com.example.gaugeapp.repositories.common.StorageRepository
import com.example.gaugeapp.repositories.creditRepositories.AirtimeCreditRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@EntryPoint
@InternalCoroutinesApi
@InstallIn(ApplicationComponent::class)
interface RepositoryProviderEntryPoint {
    fun storageRepository(): StorageRepository
    fun airtimeCreditRepository(): AirtimeCreditRepository
}

