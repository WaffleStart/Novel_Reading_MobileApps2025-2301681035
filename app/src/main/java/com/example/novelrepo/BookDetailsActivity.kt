// app/src/main/java/com/example/novelrepo/BookDetailsActivity.kt
package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.button.MaterialButton
import kotlin.math.roundToInt

class BookDetailsActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()
    private var currentBookId: Int = -1
    private var currentBook: com.example.novelrepo.data.Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        setupBottomNav()

        currentBookId = intent?.getIntExtra("book_id", -1) ?: -1
        if (currentBookId == -1) currentBookId = 1

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

        // Observe the book
        viewModel.loadBook(currentBookId).observe(this, Observer { book ->
            if (book == null) return@Observer
            currentBook = book

            // cover/title/author/description (existing code)
            val resId = resources.getIdentifier(book.coverResName, "drawable", packageName)
            if (resId != 0) coverView.setImageResource(resId) else coverView.setImageResource(R.drawable.hobbit)
            titleView.text = book.title
            authorView.text = book.author
            descView.text = book.description

            // genres (existing)
            chipGroup.removeAllViews()
            for (g in book.genreList()) {
                val chip = Chip(this)
                chip.text = g
                chip.isClickable = false
                chip.isCheckable = false
                chipGroup.addView(chip)
            }

            // --- NEW: dynamic fields ---
            pagesView.text = "${book.pages}"
            readTimeView.text = book.readTime

            // rating: if you added rating Float and ratingCount Int to Book entity
            // show star text like "★ 4.8" and rating count like "(1.2K)"
            val ratingValue = if (book.rating > 0f) book.rating else 0f
            ratingTextView.text = "★ ${"%.1f".format(ratingValue)}"
            ratingCountView.text = if (book.ratingCount > 0) "(${formatCount(book.ratingCount)})" else "(0)"

            // compute percent from chapters (fallback to percentRead)
            val percent = if (book.pages > 0) {
                ((book.currentChapter.toDouble() / book.pages.toDouble()) * 100.0).roundToInt().coerceIn(0, 100)
            } else {
                book.percentRead.coerceIn(0, 100)
            }

            // Update the button UI based on inLibrary (existing)
            updateLibraryButton(book)
        })

        btnRud.setOnClickListener {
            currentBook?.let { book ->
                viewModel.toggleInLibrary(book)
                // Immediately reflect optimistic UI change while DB updates
                val optimistic = book.copy(inLibrary = !book.inLibrary)
                currentBook = optimistic
                updateLibraryButton(optimistic)
            }
        }

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


    private fun updateLibraryButton(book: com.example.novelrepo.data.Book) {
        val btnRud = findViewById<MaterialButton>(R.id.rud_From_List)
        if (book.inLibrary) {
            btnRud.text = "Remove From List"
            // pressed / filled style
            btnRud.setBackgroundColor(resources.getColor(R.color.colorAccent, theme))
            btnRud.setTextColor(resources.getColor(R.color.colorBackgroundSecondary, theme))
            btnRud.strokeWidth = 0
            btnRud.elevation = 4f
        } else {
            btnRud.text = "Add To Library"
            // less pressed / outlined style
            btnRud.setBackgroundColor(resources.getColor(android.R.color.transparent, theme))
            btnRud.setTextColor(resources.getColor(R.color.colorTextPrimary, theme))
            btnRud.strokeWidth = resources.getDimensionPixelSize(R.dimen.outline_stroke_width) // define in dimens
            btnRud.strokeColor = resources.getColorStateList(R.color.colorPotentialSecondary, theme)
            btnRud.elevation = 0f
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.invisible
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { startActivity(Intent(this, MainActivity::class.java)); true }
                R.id.nav_discover -> { startActivity(Intent(this, DiscoverActivity::class.java)); true }
                R.id.nav_library -> { startActivity(Intent(this, LibraryActivity::class.java)); true }
                else -> false
            }
        }
    }
}
