package knightcoder.shelf.presentation.booklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import knightcoder.shelf.R
import knightcoder.shelf.databinding.FragmentBookListBinding
import knightcoder.shelf.domain.model.Book
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BookListFragment : Fragment() {

    private var _binding: FragmentBookListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookListViewModel by viewModels()

    private lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BookAdapter(
            onItemClick = { book ->
                val action = BookListFragmentDirections.actionBookListFragmentToAddEditBookFragment(book)
                findNavController().navigate(action)
            },
            onItemLongClick = { book, anchorView ->
                showDeletePopup(book, anchorView)
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeBooks()

        binding.addBookButton.setOnClickListener {
            findNavController().navigate(R.id.action_bookListFragment_to_addEditBookFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.filterEditText.addTextChangedListener {
            viewModel.setFilterQuery(it.toString())
        }

        binding.filterButton.setOnClickListener {
            viewModel.setFilterQuery(binding.filterEditText.text.toString())
        }
    }

    private fun showDeletePopup(book: Book, anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.book_item_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_book -> {
                    viewModel.deleteBook(book)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun observeBooks() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    binding.swipeRefreshLayout.isRefreshing = false

                    when (state) {
                        is BookListUiState.Loading -> {
                            binding.shimmerViewContainer.isVisible = true
                            binding.shimmerViewContainer.startShimmer()
                            binding.recyclerView.isVisible = false
                            binding.noBooks.isVisible = false
                        }

                        is BookListUiState.Success -> {
                            binding.shimmerViewContainer.isVisible = false
                            binding.shimmerViewContainer.stopShimmer()

                            val grouped = state.books.groupBy {
                                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(it.createdAt))
                            }

                            val groupedList = grouped.mapValues { entry ->
                                entry.value.sortedByDescending { it.author }
                            }

                            adapter.submitData(groupedList.toSortedMap(compareByDescending { it }))

                            binding.recyclerView.isVisible = true
                            binding.noBooks.isVisible = false
                        }

                        is BookListUiState.Empty -> {
                            binding.shimmerViewContainer.isVisible = false
                            binding.shimmerViewContainer.stopShimmer()
                            binding.recyclerView.isVisible = false
                            binding.noBooks.isVisible = true
                        }

                        is BookListUiState.Error -> {
                            binding.shimmerViewContainer.isVisible = false
                            binding.shimmerViewContainer.stopShimmer()
                            binding.recyclerView.isVisible = false
                            binding.noBooks.isVisible = true
                        }
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
