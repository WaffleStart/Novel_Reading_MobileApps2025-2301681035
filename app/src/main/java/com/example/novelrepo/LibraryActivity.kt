package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.roundToInt

class LibraryActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()
    private lateinit var adapter: LibraryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        // Adapter with callbacks wired to ViewModel
        adapter = LibraryAdapter(
            this,
            onClick = { book ->
                val intent = Intent(this, BookDetailsActivity::class.java)
                intent.putExtra("book_id", book.id)
                startActivity(intent)
            },
            onMarkComplete = { book ->
                val total = book.pages.coerceAtLeast(book.currentChapter)
                val updated = book.copy(currentChapter = total, percentRead = 100)
                viewModel.updateBook(updated)
            },
            onUpdateChapter = { book, newChapter ->
                // Compute percent from chapters if totalChapters > 0
                val percent = if (book.pages > 0) {
                    ((newChapter.toDouble() / book.pages.toDouble()) * 100.0).roundToInt().coerceIn(0, 100)
                } else {
                    book.percentRead.coerceIn(0, 100)
                }
                val updated = book.copy(currentChapter = newChapter, percentRead = percent)
                viewModel.updateBook(updated)
            },
            onRemove = { book ->
                viewModel.setInLibrary(book, false)
                Toast.makeText(this, "\"${book.title}\" removed from library", Toast.LENGTH_SHORT).show()
            }

        )

        val recycler = findViewById<RecyclerView>(R.id.libraryRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // Observe library books and submit to adapter
        viewModel.libraryBooks.observe(this) { list ->
            adapter.submitList(list)
            val emptyText = findViewById<View>(R.id.libraryEmptyText)
            emptyText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_library
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

                R.id.nav_library -> true

                R.id.nav_create -> {
                    startActivity(Intent(this, CreateBookActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
