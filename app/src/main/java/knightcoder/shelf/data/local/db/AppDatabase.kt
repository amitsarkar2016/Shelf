package knightcoder.shelf.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import knightcoder.shelf.data.local.dao.BookDao
import knightcoder.shelf.data.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
}