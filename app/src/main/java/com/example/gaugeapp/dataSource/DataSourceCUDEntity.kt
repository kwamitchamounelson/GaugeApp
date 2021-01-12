package com.example.gaugeapp.dataSource

interface DataSourceCUDEntity <T> {
    suspend fun createEntity(entity: T)
    suspend fun updateEntity(entity: T)
    suspend fun deleteEntity(entity: T)
}