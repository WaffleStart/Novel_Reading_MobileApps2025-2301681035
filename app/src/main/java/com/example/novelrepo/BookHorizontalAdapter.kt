package com.example.novelrepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.data.Book
import com.example.novelrepo.util.ResHelper

class BookHorizontalAdapter(
    private val onItemClick: (Book) -> Unit,
    private val onAddToLibraryClick: (Book) -> Unit
) : ListAdapter<Book, BookHorizontalAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val cover: ImageView = view.findViewById(R.id.recoCover)
        private val title: TextView = view.findViewById(R.id.recoTitle)
        private val author: TextView = view.findViewById(R.id.recoAuthor)

        fun bind(book: Book) {
            title.text = book.title
            author.text = book.author
            val resId = ResHelper.drawableId(itemView.context, book.coverResName, R.drawable.hobbit)
            cover.setImageResource(resId)

            itemView.setOnClickListener { onItemClick(book) }
            // If you later add an "Add" button to item_recommended_book.xml, call onAddToLibraryClick here.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended_book, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
