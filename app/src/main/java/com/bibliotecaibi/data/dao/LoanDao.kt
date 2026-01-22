package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.Loan

@Dao
interface LoanDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(loan: Loan): Long

    @Update
    suspend fun update(loan: Loan)

    @Query("SELECT * FROM loans WHERE loanId = :id LIMIT 1")
    suspend fun get(id: Long): Loan?

    @Query("""
        SELECT * FROM loans
        WHERE branchId = :branchId
        AND status = 'OPEN'
        ORDER BY dueDate ASC
    """)
    suspend fun openAtBranch(branchId: Long): List<Loan>

    @Query("""
        SELECT * FROM loans
        WHERE memberId = :memberId
        AND status = 'OPEN'
        ORDER BY loanDate DESC
    """)
    suspend fun openForMember(memberId: Long): List<Loan>
}
