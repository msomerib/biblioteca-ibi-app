package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "inventory",
    primaryKeys = ["isbn", "branchId"],
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["isbn"],
            childColumns = ["isbn"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Branch::class,
            parentColumns = ["branchId"],
            childColumns = ["branchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("branchId")]
)
data class Inventory(
    val isbn: String,
    val branchId: Long,
    val quantityTotal: Int = 0,
    val quantityAvailable: Int = 0,
    val minThreshold: Int = 0
)
