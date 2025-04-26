package knightcoder.shelf.data.local.db.migration

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val VERSION_1_2 = object: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
     database.execSQL("alter table books rename publishedDate to publishedYear")
    }
}