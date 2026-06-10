package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.novelrepo.data.AppDatabase
import com.example.novelrepo.data.Book
import kotlinx.coroutines.launch

class BookDetailsActivity : AppCompatActivity() {

    private var currentBook: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        val bookId = intent.getIntExtra("BOOK_ID", -1)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnContinue = findViewById<Button>(R.id.continue_reading)
        val title = findViewById<TextView>(R.id.detail_book_title)
        val author = findViewById<TextView>(R.id.detail_book_author)
        val cover = findViewById<ImageView>(R.id.detail_book_cover)
        val description = findViewById<TextView>(R.id.card_description)

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@BookDetailsActivity).bookDao()
            currentBook = dao.getBookById(bookId)

            currentBook?.let { book ->
                title.text = book.title
                author.text = book.author
                cover.setImageResource(book.imageRes)
                description.text = book.description
            }
        }

        btnBack.setOnClickListener { finish() }

        btnContinue.setOnClickListener {
            currentBook?.let {
                val intent = Intent(this, ReaderActivity::class.java)
                intent.putExtra("BOOK_ID", it.id)
                startActivity(intent)
            }
        }
    }
}
