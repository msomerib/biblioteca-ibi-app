package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.Inventory

@Dao
interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(inv: Inventory)

    @Query("SELECT * FROM inventory WHERE branchId = :branchId AND isbn = :isbn LIMIT 1")
    suspend fun get(branchId: Long, isbn: String): Inventory?

    @Query("""
        SELECT * FROM inventory
        WHERE branchId = :branchId
        ORDER BY isbn
    """)
    suspend fun listByBranch(branchId: Long): List<Inventory>
}
