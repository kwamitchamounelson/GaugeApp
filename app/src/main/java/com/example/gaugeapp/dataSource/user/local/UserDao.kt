package com.example.gaugeapp.dataSource.user.local

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userCacheEntity: UserCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUser(userCacheEntityList: List<UserCacheEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(userCacheEntity: UserCacheEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAllUser(userCacheEntityList: List<UserCacheEntity>)

    @Delete
    suspend fun deleteUser(userCacheEntity: UserCacheEntity)

    @Delete
    suspend fun deleteAllUser(userCacheEntityList: List<UserCacheEntity>)

    @Query("SELECT * FROM usercacheentity")
    suspend fun getUserList(): List<UserCacheEntity>

    @Query("SELECT * FROM usercacheentity WHERE userUuid =:id")
    suspend fun getUser(id: String): UserCacheEntity

    @Query("SELECT * FROM usercacheentity WHERE userUuid IN (:ids)")
    suspend fun getUserInIds(ids: List<String>): List<UserCacheEntity>
}