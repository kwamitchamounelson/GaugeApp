package com.example.gaugeapp.dataSource.communityLoan.commentComLoan.local

import com.example.gaugeapp.entities.communityLoan.CommentComLoan
import com.example.gaugeapp.utils.EntityMapper
import com.example.gaugeapp.utils.fromLong
import com.example.gaugeapp.utils.toLong
import javax.inject.Inject

class CommentComLoanLocalMapper @Inject constructor() :
    EntityMapper<CommentComLoanTable, CommentComLoan>() {
    override fun mapFromEntity(entity: CommentComLoan): CommentComLoanTable {
        return CommentComLoanTable(
            id = entity.id,
            campaignId = entity.campaignId,
            text = entity.text,
            createAt = entity.createAt.toLong(),
            parentId = entity.parentId
        )
    }

    override fun mapToEntity(storageEntity: CommentComLoanTable): CommentComLoan {
        return CommentComLoan().apply {
            id = storageEntity.id
            campaignId = storageEntity.campaignId
            text = storageEntity.text
            createAt = storageEntity.createAt.fromLong()
            parentId = storageEntity.parentId
        }
    }
}