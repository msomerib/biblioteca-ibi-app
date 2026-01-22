package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = Owner::class,
            parentColumns = ["ownerId"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("ownerId"),
        Index(value = ["username", "ownerId"], unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val ownerId: Long,
    val username: String,
    val passwordHash: String,
    val salt: String,
    val role: String = "ADMIN" // ADMIN | OPERATOR
)
