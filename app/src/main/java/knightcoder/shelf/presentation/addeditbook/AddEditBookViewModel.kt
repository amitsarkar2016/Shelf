package knightcoder.shelf.presentation.addeditbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import knightcoder.shelf.domain.model.Book
import knightcoder.shelf.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditBookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    fun insert(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertBook(book)
    }

    fun update(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateBook(book)
    }
}