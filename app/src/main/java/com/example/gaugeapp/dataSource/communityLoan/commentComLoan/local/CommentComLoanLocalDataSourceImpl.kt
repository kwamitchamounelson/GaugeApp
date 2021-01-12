package com.example.gaugeapp.dataSource.communityLoan.commentComLoan.local

import javax.inject.Inject

class CommentComLoanLocalDataSourceImpl @Inject constructor(
    private val mapper: CommentComLoanLocalMapper,
    private val dao: CommentComLoanDao
) {
}