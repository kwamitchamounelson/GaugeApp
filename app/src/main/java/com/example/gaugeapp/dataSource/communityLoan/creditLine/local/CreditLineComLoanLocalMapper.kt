package com.example.gaugeapp.dataSource.communityLoan.creditLine.local

import com.example.gaugeapp.entities.communityLoan.CreditLineComLoan
import com.example.gaugeapp.utils.EntityMapper
import com.example.gaugeapp.utils.fromLong
import com.example.gaugeapp.utils.toLong
import javax.inject.Inject

class CreditLineComLoanLocalMapper @Inject constructor() :
    EntityMapper<CreditLineComLoanTable, CreditLineComLoan>() {
    override fun mapFromEntity(entity: CreditLineComLoan): CreditLineComLoanTable {
        return CreditLineComLoanTable(
            id = entity.id,
            repaymentDate = entity.repaymentDate.toLong(),
            createDate = entity.createDate.toLong(),
            ownerId = entity.ownerId,
            solved = entity.solved
        )
    }

    override fun mapToEntity(storageEntity: CreditLineComLoanTable): CreditLineComLoan {
        return CreditLineComLoan().apply {
            id = storageEntity.id
            repaymentDate = storageEntity.repaymentDate.fromLong()
            createDate = storageEntity.createDate.fromLong()
            ownerId = storageEntity.ownerId
            solved = storageEntity.solved
        }
    }
}