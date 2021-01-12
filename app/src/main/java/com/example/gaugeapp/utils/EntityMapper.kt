package com.example.gaugeapp.utils

abstract class EntityMapper <StorageEntity, Entity> {

    abstract fun mapFromEntity (entity: Entity) : StorageEntity

    abstract fun mapToEntity (storageEntity: StorageEntity) : Entity

    fun mapListToEntity(storageList: List<StorageEntity>) : List<Entity>{
        return storageList.map{ mapToEntity(it) }
    }

    fun mapListFromEntity(list: List<Entity>) : List<StorageEntity>{
        return list.map{ mapFromEntity(it) }
    }
}