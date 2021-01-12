package com.example.gaugeapp.dataSource.user.local

import com.kola.kola_entities_features.entities.KUser
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(val userLocalMapper: UserLocalMapper, val userDao: UserDao):
    UserLocalDataSource {
    override suspend fun getUser(id: String): KUser =
        userLocalMapper.mapToEntity(userDao.getUser(id))

    override suspend fun getAllUser(): List<KUser> =
        userLocalMapper.mapListToEntity(userDao.getUserList())

    override suspend fun getAllUserIn(ids: List<String>): List<KUser> =
        userLocalMapper.mapListToEntity(userDao.getUserInIds(ids))

    override suspend fun createEntity(entity: KUser) =
        userDao.insertUser(userLocalMapper.mapFromEntity(entity))

    override suspend fun updateEntity(entity: KUser) =
        userDao.updateUser(userLocalMapper.mapFromEntity(entity))

    override suspend fun deleteEntity(entity: KUser) =
        userDao.deleteUser(userLocalMapper.mapFromEntity(entity))

    override suspend fun createAllEntity(entityList: List<KUser>) =
        userDao.insertAllUser(userLocalMapper.mapListFromEntity(entityList))

    override suspend fun updateAllEntity(entityList: List<KUser>) =
        userDao.updateAllUser(userLocalMapper.mapListFromEntity(entityList))

    override suspend fun deleteAllEntity(entityList: List<KUser>) =
        userDao.deleteAllUser(userLocalMapper.mapListFromEntity(entityList))
}