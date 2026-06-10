package com.example.novelrepo

import android.content.Context
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

class RecommendedAdapter(
    private val context: Context,
    private val onClick: (Book) -> Unit
) : ListAdapter<Book, RecommendedAdapter.ViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended_book, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.recoCover)
        private val title: TextView = itemView.findViewById(R.id.recoTitle)
        private val author: TextView = itemView.findViewById(R.id.recoAuthor)

        fun bind(book: Book) {
            // Resolve drawable resource from coverResName
            val resId = ResHelper.drawableId(itemView.context, book.coverResName, R.drawable.hobbit)
            cover.setImageResource(resId)
            cover.setBackgroundResource(resId)


            title.text = book.title
            author.text = book.author

            itemView.setOnClickListener { onClick(book) }
        }
    }

    class Diff : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
    }
}
