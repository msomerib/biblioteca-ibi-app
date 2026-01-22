package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.Member

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(member: Member): Long

    @Update
    suspend fun update(member: Member)

    @Query("SELECT * FROM members WHERE branchId = :branchId AND active = 1 ORDER BY name")
    suspend fun listActive(branchId: Long): List<Member>

    @Query("SELECT * FROM members WHERE memberId = :id LIMIT 1")
    suspend fun get(id: Long): Member?
}
