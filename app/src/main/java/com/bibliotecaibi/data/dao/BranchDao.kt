package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.Branch

@Dao
interface BranchDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(branch: Branch): Long

    @Query("SELECT * FROM branches WHERE ownerId = :ownerId ORDER BY name")
    suspend fun listByOwner(ownerId: Long): List<Branch>

    @Query("SELECT * FROM branches WHERE branchId = :id LIMIT 1")
    suspend fun get(id: Long): Branch?
}
