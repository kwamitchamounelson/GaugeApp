package com.example.gaugeapp.dataSource

interface DataSourceCUDAllEntity<T> {
    suspend fun createAllEntity(entityList: List<T>)
    suspend fun updateAllEntity(entityList: List<T>)
    suspend fun deleteAllEntity(entityList: List<T>)
}