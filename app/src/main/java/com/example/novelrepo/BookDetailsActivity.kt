package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.novelrepo.data.Book
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.button.MaterialButton

class BookDetailsActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()
    private var currentBookId: Int = -1
    private var currentBook: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        setupBottomNav()

        // Read extras: accept either "bookId" or "book_id"
        currentBookId = intent?.getIntExtra("bookId", -1) ?: -1
        if (currentBookId == -1) {
            currentBookId = intent?.getIntExtra("book_id", -1) ?: -1
        }

        // If still not provided, fall back to 1 (keeps previous behavior) but warn
        if (currentBookId == -1) {
            Toast.makeText(this, "Book id missing, opening default book.", Toast.LENGTH_SHORT).show()
            currentBookId = 1
        }

        // Views
        val backBtn = findViewById<ImageButton>(R.id.btnBack)
        val shareBtn = findViewById<ImageButton>(R.id.btnShare)
        val coverView = findViewById<ImageView>(R.id.detail_book_cover)
        val titleView = findViewById<TextView>(R.id.detail_book_title)
        val authorView = findViewById<TextView>(R.id.detail_book_author)
        val descView = findViewById<TextView>(R.id.card_description)
        val chipGroup = findViewById<ChipGroup>(R.id.genreChipGroup)
        val btnRud = findViewById<MaterialButton>(R.id.rud_From_List)
        val continueBtn = findViewById<MaterialButton>(R.id.continue_reading)
        val pagesView = findViewById<TextView>(R.id.detail_book_pages)
        val readTimeView = findViewById<TextView>(R.id.detail_book_readtime)
        val ratingTextView = findViewById<TextView>(R.id.detail_book_rating_text)
        val ratingCountView = findViewById<TextView>(R.id.detail_book_rating_count)

        // Back button
        backBtn.setOnClickListener { finish() }

        Log.d("BookDetails", "Before toggle: $currentBook")

        // Observe the book by id
        viewModel.loadBook(currentBookId).observe(this, Observer { book ->
            if (book == null) return@Observer
            currentBook = book

            // cover/title/author/description
            val resId = resources.getIdentifier(book.coverResName, "drawable", packageName)
            if (resId != 0) coverView.setImageResource(resId) else coverView.setImageResource(R.drawable.hobbit)
            titleView.text = book.title
            authorView.text = book.author
            descView.text = book.description

            // genres
            chipGroup.removeAllViews()
            for (g in book.genreList()) {
                val chip = Chip(this)
                chip.text = g
                chip.isClickable = false
                chip.isCheckable = false
                chipGroup.addView(chip)
            }

            // Share button (uses currentBook)
            // inside your observer where you bind the book (BookDetailsActivity)
            val shareBtn = findViewById<ImageButton>(R.id.btnShare)

// attach inside the observer so currentBook is available
            shareBtn.setOnClickListener {
                val b = currentBook
                if (b == null) {
                    // defensive: should not happen if you attach inside observer
                    android.util.Log.d("BookDetails", "Share clicked but currentBook is null")
                    android.widget.Toast.makeText(this, "Book not loaded yet", android.widget.Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val shareText = "${b.title} by ${b.author}\n\n${b.description}\n\nGet it in NovelRepo app."
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, b.title)
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }

                try {
                    startActivity(Intent.createChooser(intent, "Share book"))
                    android.util.Log.d("BookDetails", "Share chooser started for book id=${b.id}")
                } catch (e: Exception) {
                    android.util.Log.e("BookDetails", "Share failed", e)
                    android.widget.Toast.makeText(this, "No app available to share or error occurred", android.widget.Toast.LENGTH_SHORT).show()
                }
            }


            // dynamic fields
            pagesView.text = "${book.pages}"
            readTimeView.text = book.readTime
            val ratingValue = if (book.rating > 0f) book.rating else 0f
            ratingTextView.text = "★ ${"%.1f".format(ratingValue)}"
            ratingCountView.text = if (book.ratingCount > 0) "(${formatCount(book.ratingCount)})" else "(0)"

            // Update library button UI
            updateLibraryButton(book)
        })

        // Toggle library action
        btnRud.isEnabled = false

// Observe the book by id (inside your existing observer block)
        viewModel.loadBook(currentBookId).observe(this, Observer { book ->
            if (book == null) return@Observer
            currentBook = book

            // ... existing UI binding code ...

            // enable button now that book is loaded
            btnRud.isEnabled = true

            // set click listener here so it always has a valid book reference
            btnRud.setOnClickListener {
                val bookNow = currentBook ?: return@setOnClickListener
                val newInLibrary = !bookNow.inLibrary

                // optimistic UI update
                val optimistic = bookNow.copy(inLibrary = newInLibrary)
                currentBook = optimistic
                updateLibraryButton(optimistic)

                // persist change
                viewModel.setInLibrary(bookNow, newInLibrary)

                if (newInLibrary) {
                    Toast.makeText(this, "\"${bookNow.title}\" added to library", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "\"${bookNow.title}\" removed from library", Toast.LENGTH_SHORT).show()
                }
            }

        })

        // Continue reading
        continueBtn.setOnClickListener {
            currentBook?.let {
                val readerIntent = Intent(this, ReaderActivity::class.java)
                readerIntent.putExtra("book_id", it.id)
                startActivity(readerIntent)
            }
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
            else -> count.toString()
        }
    }

    private fun updateLibraryButton(book: Book) {
        val btnRud = findViewById<MaterialButton>(R.id.rud_From_List)
        if (book.inLibrary) {
            btnRud.text = "Remove From List"
            btnRud.setBackgroundColor(resources.getColor(R.color.colorAccent, theme))
            btnRud.setTextColor(resources.getColor(R.color.colorBackgroundSecondary, theme))
            btnRud.strokeWidth = 0
            btnRud.elevation = 4f
        } else {
            btnRud.text = "Add To Library"
            btnRud.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            btnRud.setTextColor(resources.getColor(R.color.colorTextPrimary, theme))
            // If you don't have the dimension, fallback to 2
            val stroke = try {
                resources.getDimensionPixelSize(R.dimen.outline_stroke_width)
            } catch (e: Exception) {
                2
            }
            btnRud.strokeWidth = stroke
            btnRud.strokeColor = resources.getColorStateList(R.color.colorPotentialSecondary, theme)
            btnRud.elevation = 0f
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        // keep current selection neutral
        bottomNav.selectedItemId = R.id.invisible
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { startActivity(Intent(this, MainActivity::class.java)); true }
                R.id.nav_discover -> { startActivity(Intent(this, DiscoverActivity::class.java)); true }
                R.id.nav_library -> { startActivity(Intent(this, LibraryActivity::class.java)); true }
                R.id.nav_create -> { startActivity(Intent(this, CreateBookActivity::class.java)); true }
                else -> false
            }
        }
    }
}
