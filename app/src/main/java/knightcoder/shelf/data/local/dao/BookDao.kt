package knightcoder.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import knightcoder.shelf.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>) // New: bulk insert

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    suspend fun getAllBooksOnce(): List<BookEntity>

    @Query("SELECT * FROM books WHERE author LIKE '%' || :author || '%' ORDER BY createdAt DESC")
    fun getBookByAuthor(author: String): Flow<List<BookEntity>>
}
