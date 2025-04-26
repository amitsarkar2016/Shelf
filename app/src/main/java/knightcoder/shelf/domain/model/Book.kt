package knightcoder.shelf.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Int = 0,
    val title: String,
    val author: String,
    val publishedYear: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
): Parcelable
