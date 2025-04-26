package knightcoder.shelf.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import knightcoder.shelf.data.local.dao.BookDao
import knightcoder.shelf.data.local.db.AppDatabase
import knightcoder.shelf.data.local.db.migration.VERSION_1_2
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "shelf_db")
            .addMigrations(VERSION_1_2)
            .build()
    }

    @Provides
    fun provideBokDao(database: AppDatabase): BookDao = database.bookDao()
}
