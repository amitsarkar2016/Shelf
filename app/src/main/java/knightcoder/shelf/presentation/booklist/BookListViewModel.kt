package knightcoder.shelf.presentation.booklist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import knightcoder.shelf.domain.model.Book
import knightcoder.shelf.domain.repository.BookRepository
import knightcoder.shelf.utils.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: BookRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _filterQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val uiState: StateFlow<BookListUiState> = _uiState

    private var job: Job? = null
    private var booksJob: Job? = null

    init {
        observeNetwork()
        fetchBooks()
    }

    private fun fetchBooks() {
        booksJob?.cancel()
        booksJob = viewModelScope.launch {
            _filterQuery
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        repository.getAllBooks()
                    } else {
                        repository.getBooksByAuthor(query)
                    }
                }
                .map { books ->
                    delay(300)
                    if (books.isEmpty()) {
                        BookListUiState.Empty
                    } else {
                        BookListUiState.Success(books)
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = BookListUiState.Loading
            repository.refreshBooksFromApi()
            delay(300)
            fetchBooks()
        }
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            NetworkMonitor.networkStatus(context).collect { isConnected ->
                if (isConnected) {
                    refresh()
                }
            }
        }
    }

    fun setFilterQuery(query: String) {
        _uiState.value = BookListUiState.Loading
        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            _filterQuery.value = query
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBook(book)
        }
    }
}
