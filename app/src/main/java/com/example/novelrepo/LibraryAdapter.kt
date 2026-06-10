package com.example.novelrepo

import android.app.AlertDialog
import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.data.Book
import kotlin.math.roundToInt

class LibraryAdapter(
    private val context: Context,
    private val onClick: (Book) -> Unit,
    private val onMarkComplete: (Book) -> Unit,
    private val onUpdateChapter: (Book, Int) -> Unit,
    private val onRemove: (Book) -> Unit
) : ListAdapter<Book, LibraryAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_library_book, parent, false)
        return BookViewHolder(v)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.rowCover)
        private val title: TextView = itemView.findViewById(R.id.rowTitle)
        private val author: TextView = itemView.findViewById(R.id.rowAuthor)
        private val progress: ProgressBar = itemView.findViewById(R.id.rowProgress)
        private val chapterText: TextView = itemView.findViewById(R.id.chapterProgress)
        private val dotMenu: ImageButton = itemView.findViewById(R.id.dotMenu)

        fun bind(book: Book) {
            // Resolve drawable resource from coverResName
            val resId = context.resources.getIdentifier(book.coverResName, "drawable", context.packageName)
            if (resId != 0) cover.setImageResource(resId) else cover.setImageResource(R.drawable.hobbit)

            title.text = book.title
            author.text = book.author

            // Compute percent from chapter if pages available, else fallback to percentRead
            val percent = computePercent(book)
            progress.progress = percent
            val chapterDisplay = "Chapter ${book.currentChapter.coerceAtLeast(0)} - ${percent}%"
            chapterText.text = chapterDisplay

            itemView.setOnClickListener { onClick(book) }

            // Dot menu popup
            dotMenu.setOnClickListener { view ->
                showPopupForBook(book)
            }
        }

        private fun computePercent(book: Book): Int {
            // If pages > 0, compute from currentChapter; else fallback to percentRead
            val total = book.pages
            val current = book.currentChapter
            return if (total > 0) {
                val raw = (current.toDouble() / total.toDouble()) * 100.0
                raw.roundToInt().coerceIn(0, 100)
            } else {
                // fallback to stored percentRead if present
                book.percentRead.coerceIn(0, 100)
            }
        }

        private fun showPopupForBook(book: Book) {
            val options = arrayOf("Mark complete", "Update chapter", "Remove from library")
            AlertDialog.Builder(context)
                .setTitle(book.title)
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> {
                            // Mark complete
                            onMarkComplete(book)
                        }
                        1 -> {
                            // Update chapter -> show input dialog
                            showUpdateChapterDialog(book)
                        }
                        2 -> {
                            // Remove
                            AlertDialog.Builder(context)
                                .setTitle("Remove")
                                .setMessage("Remove \"${book.title}\" from your library?")
                                .setPositiveButton("Remove") { _, _ -> onRemove(book) }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }
                    }
                }
                .show()
        }

        private fun showUpdateChapterDialog(book: Book) {
            val total = book.pages.coerceAtLeast(1) // avoid zero division; if zero, allow any positive
            val input = EditText(context)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            input.hint = "${book.currentChapter}"
            // Optional: limit length to reasonable digits
            input.filters = arrayOf(InputFilter.LengthFilter(5))

            AlertDialog.Builder(context)
                .setTitle("Update chapter")
                .setMessage("Enter current chapter (0 - $total)")
                .setView(input)
                .setPositiveButton("Update") { _, _ ->
                    val text = input.text.toString().trim()
                    val newChapter = text.toIntOrNull()
                    if (newChapter == null) {
                        // invalid input: ignore or show error
                        AlertDialog.Builder(context)
                            .setMessage("Please enter a valid number between 0 and $total.")
                            .setPositiveButton("OK", null)
                            .show()
                    } else {
                        val clamped = newChapter.coerceIn(0, total)
                        onUpdateChapter(book, clamped)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
    }
}
