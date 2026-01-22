package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND ownerId = :ownerId LIMIT 1")
    suspend fun findByUsername(ownerId: Long, username: String): User?
}
