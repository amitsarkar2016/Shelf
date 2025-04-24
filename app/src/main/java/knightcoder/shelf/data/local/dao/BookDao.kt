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

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("select * from books order by createdAt desc")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("select * from books where author like '%' || :author || '%' order by createdAt desc")
    fun getBookByAuthor(author: String) : Flow<List<BookEntity>>
}