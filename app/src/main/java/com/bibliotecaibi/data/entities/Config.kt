package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "config",
    foreignKeys = [
        ForeignKey(
            entity = Owner::class,
            parentColumns = ["ownerId"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ownerId"], unique = true)]
)
data class Config(
    @PrimaryKey(autoGenerate = true) val configId: Long = 0,
    val ownerId: Long,
    val defaultLoanDays: Int = 7,
    val memberLoanLimit: Int = 3
)
