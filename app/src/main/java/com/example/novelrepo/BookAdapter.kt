package com.example.novelrepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.data.Book

class BookAdapter(
    private val items: List<Book>,
    private val onClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.book_title)
        private val author = view.findViewById<TextView>(R.id.book_author)
        private val progressText = view.findViewById<TextView>(R.id.book_progress)
        private val progressBar = view.findViewById<ProgressBar>(R.id.book_progress_bar)
        private val cover = view.findViewById<ImageView>(R.id.book_cover)
        private val root = view.findViewById<View>(R.id.book_item_root)

        fun bind(book: Book) {
            title.text = book.title
            author.text = book.author
            progressText.text = "Progress: ${book.progress}%"
            progressBar.progress = book.progress
            cover.setImageResource(book.coverRes)

            root.setOnClickListener {
                onClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_library, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
