package knightcoder.shelf.data.repository

import android.content.Context
import knightcoder.shelf.data.local.dao.BookDao
import knightcoder.shelf.data.mapper.toDomain
import knightcoder.shelf.data.mapper.toEntity
import knightcoder.shelf.data.remote.BookApiService
import knightcoder.shelf.domain.model.Book
import knightcoder.shelf.domain.repository.BookRepository
import knightcoder.shelf.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val context: Context,
    private val bookDao: BookDao,
    private val bookApiService: BookApiService
) : BookRepository {

    override suspend fun insertBook(book: Book) {
        bookDao.insertBook(book.toEntity())
    }

    override suspend fun updateBook(book: Book) {
        bookDao.updateBook(book.toEntity())
    }

    override suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book.toEntity())
    }

    override fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks().map { list -> list.map { it.toDomain() } }
    }

    override fun getBooksByAuthor(author: String): Flow<List<Book>> {
        return bookDao.getBookByAuthor(author).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun refreshBooksFromApi() {
        try {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                println("No Internet Connection, skipping API fetch.")
                return
            }

            val response = bookApiService.getAllBooks()
            if (response.isSuccessful) {
                val allBooksResponse = response.body()
                val remoteBooks = allBooksResponse?.data ?: emptyList()

                val localBooks = bookDao.getAllBooksOnce()
                val localBookIds = localBooks.map { it.id }.toSet()

                val newBooks = remoteBooks
                    .filter { it.id !in localBookIds }
                    .map { it.toEntity() }

                if (newBooks.isNotEmpty()) {
                    bookDao.insertBooks(newBooks)
                }
            } else {
                println("API Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
