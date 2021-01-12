package com.example.gaugeapp.dataSource.communityLoan.campaign.local

import com.example.gaugeapp.entities.communityLoan.CampaignComLoan
import com.example.gaugeapp.utils.EntityMapper
import com.example.gaugeapp.utils.fromLong
import com.example.gaugeapp.utils.toLong
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CampaignComLoanLocalMapper @Inject constructor() :
    EntityMapper<CampaignComLoanTable, CampaignComLoan>() {
    override fun mapFromEntity(entity: CampaignComLoan): CampaignComLoanTable {
        return CampaignComLoanTable(
            id = entity.id,
            creditLineId = entity.creditLineId,
            ownerId = entity.ownerId,
            description = entity.description,
            payBackDate = entity.payBackDate.toLong(),
            amount = entity.amount,
            reason = entity.reason,
            percentInterest = entity.percentInterest,
            contributionsListString = Json.encodeToString(entity.contributionsList),
            contributorIdListString = Json.encodeToString(entity.contributorIdList),
            guarantorsListString = Json.encodeToString(entity.guarantorsList),
            guarantorIdListString = Json.encodeToString(entity.guarantorIdList),
            payBackListString = Json.encodeToString(entity.payBackList),
            createAt = entity.createAt.toLong(),
            synDate = entity.synDate.toLong(),
            enable = entity.enable,
            solved = entity.solved,
            commentCount = entity.commentCount
        )
    }

    override fun mapToEntity(storageEntity: CampaignComLoanTable): CampaignComLoan {
        return CampaignComLoan().apply {
            id = storageEntity.id
            creditLineId = storageEntity.creditLineId
            ownerId = storageEntity.ownerId
            description = storageEntity.description
            payBackDate = storageEntity.payBackDate.fromLong()
            amount = storageEntity.amount
            reason = storageEntity.reason
            percentInterest = storageEntity.percentInterest
            contributionsList = Json.decodeFromString(storageEntity.contributionsListString)
            contributorIdList = Json.decodeFromString(storageEntity.contributorIdListString)
            guarantorsList = Json.decodeFromString(storageEntity.guarantorsListString)
            guarantorIdList = Json.decodeFromString(storageEntity.guarantorIdListString)
            payBackList = Json.decodeFromString(storageEntity.payBackListString)
            createAt = storageEntity.createAt.fromLong()
            synDate = storageEntity.synDate.fromLong()
            enable = storageEntity.enable
            solved = storageEntity.solved
            commentCount = storageEntity.commentCount
        }
    }
}