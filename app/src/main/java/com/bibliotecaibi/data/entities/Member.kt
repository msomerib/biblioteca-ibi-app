package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "members",
    foreignKeys = [
        ForeignKey(
            entity = Branch::class,
            parentColumns = ["branchId"],
            childColumns = ["branchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("branchId"),
        Index(value = ["branchId", "documentId"], unique = true)
    ]
)
data class Member(
    @PrimaryKey(autoGenerate = true) val memberId: Long = 0,
    val branchId: Long,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val documentId: String? = null,
    val address: String? = null,
    val birthDate: Long? = null,
    val planCategory: String? = null,
    val active: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
