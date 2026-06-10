package com.example.novelrepo

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.novelrepo.data.AppDatabase
import kotlinx.coroutines.launch

class ReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        val bookId = intent.getIntExtra("BOOK_ID", -1)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val chapterTitle = findViewById<TextView>(R.id.chapterTitle)
        val readerText = findViewById<TextView>(R.id.readerText)

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@ReaderActivity).bookDao()
            val book = dao.getBookById(bookId)

            chapterTitle.text = book?.title ?: "Chapter"
            readerText.text = book?.content ?: "No content available."
        }

        btnBack.setOnClickListener { finish() }
    }
}
