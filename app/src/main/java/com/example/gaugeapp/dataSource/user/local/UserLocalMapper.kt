package com.example.gaugeapp.dataSource.user.local

import com.kola.kola_entities_features.entities.KUser
import com.example.gaugeapp.utils.EntityMapper
import com.example.gaugeapp.utils.fromLong
import com.example.gaugeapp.utils.toLong
import javax.inject.Inject

class UserLocalMapper @Inject constructor(): EntityMapper<UserCacheEntity, KUser>() {
    override fun mapFromEntity(entity: KUser): UserCacheEntity {
        return UserCacheEntity(
            userUuid = entity.userUid,
            userName = entity.userName,
            userCreatedDate = entity.createdDate.toLong(),
            fullName = entity.fullName,
            email = entity.email,
            imageUrl = entity.imageUrL,
            authPhoneNumber = entity.authPhoneNumber,
            detectedName = entity.detectedName,
            mobileMoneyNumbers = entity.mobileMoneyNumbers.toString(),
            phoneNumbers = entity.phoneNumbers.toString()
        )
    }

    override fun mapToEntity(storageEntity: UserCacheEntity): KUser {
        return KUser(
            userUid = storageEntity.userUuid,
            userName = storageEntity.userName,
            createdDate = storageEntity.userCreatedDate.fromLong(),
            fullName = storageEntity.fullName,
            email = storageEntity.email,
            imageUrL = storageEntity.imageUrl,
            authPhoneNumber = storageEntity.authPhoneNumber,
            mobileMoneyNumbers = arrayListOf()
        )

    }
}