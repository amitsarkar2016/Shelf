package knightcoder.shelf.data.mapper

import android.os.Build
import knightcoder.shelf.data.local.entity.BookEntity
import knightcoder.shelf.data.remote.dto.BookDto
import knightcoder.shelf.domain.model.Book
import java.time.Instant

fun BookEntity.toDomain(): Book {
    return Book(id, title, author, publishedDate, description, createdAt)
}

fun Book.toEntity(): BookEntity {
    return BookEntity(id, title, author, publishedYear, description, createdAt)
}

fun BookDto.toEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = Title,
        author = Publisher,
        publishedDate = Year.toString(),
        description = Notes.joinToString(separator = "\n"),
        createdAt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Instant.parse(created_at).toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        } else {
            System.currentTimeMillis()
        }
    )
}