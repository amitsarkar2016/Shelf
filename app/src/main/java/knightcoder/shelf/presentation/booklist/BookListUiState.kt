package knightcoder.shelf.presentation.booklist

import knightcoder.shelf.domain.model.Book

sealed class BookListUiState {
    data object Loading : BookListUiState()
    data class Success(val books: List<Book>) : BookListUiState()
    data object Empty : BookListUiState()
    data class Error(val message: String) : BookListUiState()
}
