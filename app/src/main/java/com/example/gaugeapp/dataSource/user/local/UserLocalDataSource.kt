package com.example.gaugeapp.dataSource.user.local

import com.kola.kola_entities_features.entities.KUser
import com.example.gaugeapp.dataSource.DataSourceCUDAllEntity
import com.example.gaugeapp.dataSource.DataSourceCUDEntity

interface UserLocalDataSource : DataSourceCUDEntity<KUser>, DataSourceCUDAllEntity<KUser> {
    suspend fun getUser(id: String): KUser
    suspend fun getAllUser(): List<KUser>
    suspend fun getAllUserIn(ids: List<String>): List<KUser>
}