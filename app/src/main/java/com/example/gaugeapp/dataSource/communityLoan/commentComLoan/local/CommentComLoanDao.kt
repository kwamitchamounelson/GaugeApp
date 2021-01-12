package com.example.gaugeapp.dataSource.communityLoan.commentComLoan.local

import androidx.room.*
import com.example.gaugeapp.dataSource.communityLoan.creditLine.local.CreditLineComLoanTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentComLoanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(commentComLoanTable: CommentComLoanTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManyComments(commentComLoanTableList: List<CommentComLoanTable>)

    @Update
    suspend fun updateComment(commentComLoanTable: CommentComLoanTable)

    @Update
    suspend fun updateManyComments(commentComLoanTableList: List<CommentComLoanTable>)

    @Delete
    suspend fun deleteComment(commentComLoanTable: CommentComLoanTable)

    @Delete
    suspend fun deleteManyComments(commentComLoanTableList: List<CommentComLoanTable>)

    @Query("SELECT * FROM commentcomloantable ORDER BY createAt DESC")
    fun getAllCommentOfCampaign(): Flow<List<CreditLineComLoanTable>>
}