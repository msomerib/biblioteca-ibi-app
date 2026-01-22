package com.bibliotecaibi.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "owners",
    indices = [Index(value = ["name"], unique = true)]
)
data class Owner(
    @PrimaryKey(autoGenerate = true) val ownerId: Long = 0,
    val name: String,
    val contactEmail: String? = null,
    val contactPhone: String? = null
)
