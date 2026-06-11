package com.example.novelrepo

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.novelrepo.data.Book
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.ImageButton
import android.widget.Toast

class CreateBookActivity : AppCompatActivity() {
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)

        // Correct ViewModelProvider usage for AndroidViewModel
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application as Application)
        )[BookViewModel::class.java]

        val titleInput = findViewById<EditText>(R.id.inputTitle)
        val authorInput = findViewById<EditText>(R.id.inputAuthor)
        val descriptionInput = findViewById<EditText>(R.id.inputDescription)
        val genresInput = findViewById<EditText>(R.id.inputGenres)
        val pagesInput = findViewById<EditText>(R.id.inputPages)
        val readTimeInput = findViewById<EditText>(R.id.inputReadTime)
        val coverInput = findViewById<EditText>(R.id.inputCoverRes)
        val saveBtn = findViewById<Button>(R.id.btnSave)
        val backBtn = findViewById<ImageButton?>(R.id.btnCreateBack)

        backBtn?.setOnClickListener { finish() }

        saveBtn.setOnClickListener {
            val book = Book(
                id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                title = titleInput.text.toString().ifBlank { "Untitled" },
                author = authorInput.text.toString().ifBlank { "Unknown" },
                description = descriptionInput.text.toString(),
                genres = genresInput.text.toString(),
                pages = pagesInput.text.toString().toIntOrNull() ?: 0,
                readTime = readTimeInput.text.toString(),
                coverResName = coverInput.text.toString(),
                isNewRelease = true
            )
            viewModel.createBook(book)
            Toast.makeText(this, "Book created", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        // Layout uses id bottomBar — use that id
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomBar)
        // mark current item
        bottomNav.selectedItemId = R.id.nav_create

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_discover -> {
                    startActivity(Intent(this, DiscoverActivity::class.java))
                    true
                }
                R.id.nav_library -> {
                    startActivity(Intent(this, LibraryActivity::class.java))
                    true
                }
                R.id.nav_create -> true
                else -> false
            }
        }
    }
}
