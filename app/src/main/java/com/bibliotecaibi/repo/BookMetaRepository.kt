package com.bibliotecaibi.repo

import com.bibliotecaibi.ui.books.BookMeta
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ------------ Google Books API ------------
interface GoogleBooksApi {
    @GET("books/v1/volumes")
    suspend fun search(@Query("q") query: String): GoogleBookResult
}

data class GoogleBookResult(val items: List<GoogleBookItem>?)
data class GoogleBookItem(val volumeInfo: GoogleVolumeInfo?)
data class GoogleVolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?
)

// ------------ OpenLibrary API ------------
interface OpenLibraryApi {
    @GET("api/books?format=json&jscmd=data")
    suspend fun getByIsbn(@Query("bibkeys") key: String): Map<String, OpenLibBook>
}

data class OpenLibBook(
    val title: String?,
    val authors: List<OpenLibAuthor>?,
    val publishers: List<OpenLibPublisher>?,
    val publish_date: String?
)

data class OpenLibAuthor(val name: String?)
data class OpenLibPublisher(val name: String?)

object BookMetaRepository {

    private val google = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleBooksApi::class.java)

    private val openLib = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenLibraryApi::class.java)

    suspend fun fetch(isbn: String): BookMeta? {

        // 1) Google Books
        runCatching {
            val res = google.search("isbn:$isbn")
            val info = res.items?.firstOrNull()?.volumeInfo
            if (info != null) {
                return BookMeta(
                    title = info.title,
                    author = info.authors?.joinToString(", "),
                    publisher = info.publisher,
                    year = info.publishedDate?.take(4)?.toIntOrNull()
                )
            }
        }

        // 2) OpenLibrary
        runCatching {
            val map = openLib.getByIsbn("ISBN:$isbn")
            val item = map["ISBN:$isbn"] ?: return@runCatching

            return BookMeta(
                title = item.title,
                author = item.authors?.mapNotNull { it.name }?.joinToString(", "),
                publisher = item.publishers?.mapNotNull { it.name }?.firstOrNull(),
                year = item.publish_date?.takeLast(4)?.toIntOrNull()
            )
        }

        return null
    }
}
