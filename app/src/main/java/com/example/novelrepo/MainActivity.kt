package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.data.Book
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.novelrepo.util.ResHelper

class MainActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()
    private lateinit var recommendedAdapter: RecommendedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNav()

        // Views
        val currentTitle = findViewById<TextView>(R.id.currentTitle)
        val currentProgressText = findViewById<TextView>(R.id.currentProgress)
        val currentCover = findViewById<ImageView>(R.id.currentCover)
        val currentProgressBar = findViewById<ProgressBar>(R.id.progressBarMain)
        val currentContainer = findViewById<View>(R.id.currentlyReadingCard)

        // Recommended RecyclerView (horizontal) using the new adapter
        val recommendedRv = findViewById<RecyclerView>(R.id.recommendedRecycler)
        recommendedRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedAdapter = RecommendedAdapter(this) { book -> openDetails(book) }
        recommendedRv.adapter = recommendedAdapter

        // Observe all books and bind UI
        viewModel.allBooks.observe(this) { books ->
            bindCurrentlyReading(books, currentTitle, currentProgressText, currentCover, currentProgressBar, currentContainer)
            // Recommended: pick top 6 or fewer
            recommendedAdapter.submitList(books.take(6))
        }
    }

    private fun bindCurrentlyReading(
        books: List<Book>,
        titleView: TextView,
        progressTextView: TextView,
        coverView: ImageView,
        progressBar: ProgressBar,
        container: View
    ) {
        val currentlyReading = books.firstOrNull { it.percentRead in 1..99 } ?: books.firstOrNull()
        if (currentlyReading == null) {
            titleView.text = getString(R.string.no_current_book)
            progressTextView.text = ""
            coverView.setImageResource(R.color.colorSurface)
            progressBar.progress = 0
            container.isClickable = false
            return
        }

        titleView.text = currentlyReading.title
        progressBar.progress = currentlyReading.percentRead.coerceIn(0, 100)
        progressTextView.text = "${currentlyReading.percentRead}% • ${currentlyReading.readTime}"

        val res = ResHelper.drawableId(this, currentlyReading.coverResName, R.drawable.hobbit)
        coverView.setImageResource(res)

        container.isClickable = true
        container.setOnClickListener { openDetails(currentlyReading) }
    }

    private fun openDetails(book: Book) {
        val intent = Intent(this, BookDetailsActivity::class.java)
        intent.putExtra("book_id", book.id)
        startActivity(intent)
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_discover -> {
                    startActivity(Intent(this, DiscoverActivity::class.java))
                    true
                }
                R.id.nav_library -> {
                    startActivity(Intent(this, LibraryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
