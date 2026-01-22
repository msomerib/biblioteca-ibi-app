package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.Config

@Dao
interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(config: Config): Long

    @Query("SELECT * FROM config WHERE ownerId = :ownerId LIMIT 1")
    suspend fun getByOwner(ownerId: Long): Config?
}
