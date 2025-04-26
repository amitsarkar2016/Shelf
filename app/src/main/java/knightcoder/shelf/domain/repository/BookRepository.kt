package knightcoder.shelf.domain.repository

import knightcoder.shelf.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun insertBook(book: Book)

    suspend fun updateBook(book: Book)

    suspend fun deleteBook(book: Book)
    fun getAllBooks(): Flow<List<Book>>

    fun getBooksByAuthor(author: String): Flow<List<Book>>

    suspend fun refreshBooksFromApi()
}
