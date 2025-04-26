package knightcoder.shelf.presentation.booklist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import knightcoder.shelf.R
import knightcoder.shelf.domain.model.Book

class BookAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onItemLongClick: (Book, View) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val groupedData = linkedMapOf<String, List<Book>>()
    private val flatList = mutableListOf<Any>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: Map<String, List<Book>>) {
        groupedData.clear()
        flatList.clear()

        groupedData.putAll(data)
        data.forEach { (date, books) ->
            flatList.add(date)
            flatList.addAll(books)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (flatList[position] is String) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_header, parent, false)
            DateViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
            BookViewHolder(view)
        }
    }

    override fun getItemCount(): Int = flatList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DateViewHolder) {
            holder.bind(flatList[position] as String)
        } else if (holder is BookViewHolder) {
            holder.bind(flatList[position] as Book, onItemClick, onItemLongClick)
        }
    }

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(date: String) {
            itemView.findViewById<TextView>(R.id.dateHeader).text = date
        }
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(book: Book, onItemClick: (Book) -> Unit, onItemLongClick: (Book, View) -> Unit) {
            itemView.findViewById<TextView>(R.id.titleText).text = book.title
            itemView.findViewById<TextView>(R.id.authorText).text = book.author
            itemView.findViewById<TextView>(R.id.publishedText).text = book.publishedYear
            itemView.findViewById<TextView>(R.id.descText).text = book.description

            itemView.setOnClickListener { onItemClick(book) }
            itemView.setOnLongClickListener {
                onItemLongClick(book, itemView)
                true
            }
        }
    }
}
