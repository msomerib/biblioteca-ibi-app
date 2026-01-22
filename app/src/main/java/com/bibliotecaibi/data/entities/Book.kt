package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["isbn"], unique = true),
        Index("title")
    ]
)
data class Book(
    @PrimaryKey val isbn: String,
    val title: String,
    val author: String? = null,
    val publisher: String? = null,
    val year: Int? = null,
    val coverUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
