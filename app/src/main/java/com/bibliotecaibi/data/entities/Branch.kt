package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "branches",
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
        Index(value = ["name", "ownerId"], unique = true)
    ]
)
data class Branch(
    @PrimaryKey(autoGenerate = true) val branchId: Long = 0,
    val ownerId: Long,
    val name: String,
    val addressLine1: String,
    val addressLine2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null
)
