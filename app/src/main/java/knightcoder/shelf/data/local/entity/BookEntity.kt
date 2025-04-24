package knightcoder.shelf.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val publishedDate: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
)