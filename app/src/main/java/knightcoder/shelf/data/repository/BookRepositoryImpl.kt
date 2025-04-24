package knightcoder.shelf.data.repository

import knightcoder.shelf.data.local.dao.BookDao
import knightcoder.shelf.data.mapper.toDomain
import knightcoder.shelf.data.mapper.toEntity
import knightcoder.shelf.domain.model.Book
import knightcoder.shelf.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val bookDao: BookDao): BookRepository {
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
        return bookDao.getAllBooks().map { list -> list.map { it.toDomain() }}
    }

    override fun getBooksByAuthor(author: String): Flow<List<Book>> {
        return bookDao.getBookByAuthor(author).map { list-> list.map { it.toDomain() } }
    }

}