package com.bibliotecaibi.data.entities

import androidx.room.*

@Entity(
    tableName = "loans",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["isbn"],
            childColumns = ["isbn"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Member::class,
            parentColumns = ["memberId"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Branch::class,
            parentColumns = ["branchId"],
            childColumns = ["branchId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("isbn"),
        Index("memberId"),
        Index("branchId")
    ]
)
data class Loan(
    @PrimaryKey(autoGenerate = true) val loanId: Long = 0,
    val isbn: String,
    val memberId: Long,
    val branchId: Long,
    val loanDate: Long,
    val dueDate: Long,
    val returnDate: Long? = null,
    val status: String = "OPEN" // OPEN | RETURNED | LATE
)
