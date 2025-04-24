package knightcoder.shelf.data.mapper

import knightcoder.shelf.data.local.entity.BookEntity
import knightcoder.shelf.domain.model.Book

fun BookEntity.toDomain(): Book {
    return Book(id, title, author, publishedDate, description, createdAt)
}

fun Book.toEntity(): BookEntity {
    return BookEntity(id, title, author, publishedDate, description, createdAt)
}