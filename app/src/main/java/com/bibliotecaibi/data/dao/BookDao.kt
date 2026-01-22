package com.bibliotecaibi.data.dao

import androidx.room.*
import com.bibliotecaibi.data.entities.Book

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsert(book: Book): Long

    @Query("SELECT * FROM books WHERE isbn = :isbn LIMIT 1")
    suspend fun get(isbn: String): Book?

    @Query("""
        SELECT * FROM books 
        WHERE title LIKE '%' || :q || '%' 
        ORDER BY title 
        LIMIT :limit OFFSET :offset
    """)
    suspend fun search(q: String, limit: Int = 50, offset: Int = 0): List<Book>
}
