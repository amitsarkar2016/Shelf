package knightcoder.shelf.presentation.addeditbook

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import knightcoder.shelf.databinding.FragmentAddEditBookBinding
import knightcoder.shelf.domain.model.Book
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditBookFragment : Fragment() {

    private var _binding: FragmentAddEditBookBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddEditBookViewModel by viewModels()
    private var isEditing = false
    private var existingBook: Book? = null

    private val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("book", Book::class.java)?.let {
                isEditing = true
                existingBook = it
                fillForm(it)
            }
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<Book>("book")?.let {
                isEditing = true
                existingBook = it
                fillForm(it)
            }
        }

//        binding.dateEditText.setOnClickListener {
//            showDatePickerDialog()
//        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val author = binding.authorEditText.text.toString().ifBlank { "Unknown" }
            val date = binding.dateEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (title.isBlank()) {
                binding.titleEditText.error = "Title can't be empty."
                return@setOnClickListener
            }
            if (!isValidDate(date)) {
                binding.dateEditText.error = "Invalid date format. Use YYYY"
                return@setOnClickListener
            }

            val book = Book(
                id = existingBook?.id ?: 0,
                title = title,
                author = author,
                publishedYear = date,
                description = description,
                createdAt = existingBook?.createdAt ?: System.currentTimeMillis()
            )

            if (isEditing) viewModel.update(book) else viewModel.insert(book)
            findNavController().popBackStack()
        }
    }

    private fun fillForm(book: Book) {
        binding.titleEditText.setText(book.title)
        binding.authorEditText.setText(book.author)
        binding.dateEditText.setText(book.publishedYear)
        binding.descriptionEditText.setText(book.description)
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            dateFormat.isLenient = false
            dateFormat.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val formattedMonth = String.format("%02d", month + 1)
                val formattedDay = String.format("%02d", dayOfMonth)

                val date = "$year-$formattedMonth-$formattedDay"
                binding.dateEditText.setText(date)
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
