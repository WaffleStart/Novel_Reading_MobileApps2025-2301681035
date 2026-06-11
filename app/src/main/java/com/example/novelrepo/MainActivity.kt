package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.data.Book
import com.example.novelrepo.util.ResHelper
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BookViewModel
    private lateinit var recommendedAdapter: BookHorizontalAdapter
    private lateinit var newReleasesAdapter: BookHorizontalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain AndroidViewModel safely
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[BookViewModel::class.java]

        setupBottomNav()

        // Views for currently reading
        val currentTitle = findViewById<TextView>(R.id.currentTitle)
        val currentProgressText = findViewById<TextView>(R.id.currentProgress)
        val currentCover = findViewById<ImageView>(R.id.currentCover)
        val currentProgressBar = findViewById<ProgressBar>(R.id.progressBarMain)
        val currentContainer = findViewById<View>(R.id.currentlyReadingCard)

        // Recommended RecyclerView (horizontal)
        val recommendedRv = findViewById<RecyclerView>(R.id.recommendedRecycler)
        recommendedRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedAdapter = BookHorizontalAdapter(
            onItemClick = { book -> openDetails(book) },
            onAddToLibraryClick = { book -> viewModel.toggleInLibrary(book) }
        )
        recommendedRv.adapter = recommendedAdapter

        // New Releases RecyclerView (horizontal)
        val newReleasesRv = findViewById<RecyclerView>(R.id.newReleasesRecycler)
        newReleasesRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        newReleasesAdapter = BookHorizontalAdapter(
            onItemClick = { book -> openDetails(book) },
            onAddToLibraryClick = { book -> viewModel.toggleInLibrary(book) }
        )
        newReleasesRv.adapter = newReleasesAdapter

        // Observe all books and update UI
        viewModel.allBooks.observe(this) { books ->
            bindCurrentlyReading(books, currentTitle, currentProgressText, currentCover, currentProgressBar, currentContainer)

            // Recommended: show first 6 non-new-release books (adjust as desired)
            val recommended = books.filter { !it.isNewRelease }.take(6)
            recommendedAdapter.submitList(recommended)

            // New Releases: show all books marked as new releases
            val newReleases = books.filter { it.isNewRelease }
            newReleasesAdapter.submitList(newReleases)
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
        // BookDetailsActivity expects "bookId" or "book_id" depending on your implementation.
        // Use the key your details activity reads. Here we use "bookId".
        intent.putExtra("bookId", book.id)
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
                R.id.nav_create -> {
                    startActivity(Intent(this, CreateBookActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
