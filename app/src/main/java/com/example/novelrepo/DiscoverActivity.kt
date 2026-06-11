package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiscoverActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)

        setupBottomNav()

        // Example bindings for the "Trending Now" card
        val trendingTitle = findViewById<TextView>(R.id.txtTitle)
        val trendingAuthor = findViewById<TextView>(R.id.txtAuthor)
        val trendingDesc = findViewById<TextView>(R.id.txtDescription)
        val trendingBackground = findViewById<ImageView>(R.id.imgBackground)

        // New releases covers (ids in layout: imgCover1, imgCover2, imgCover3)
        val cover1 = findViewById<ImageView>(R.id.imgCover1)
        val title1 = findViewById<TextView>(R.id.txtTitle1)
        val author1 = findViewById<TextView>(R.id.txtAuthor1)
        val cover2 = findViewById<ImageView>(R.id.imgCover2)
        val title2 = findViewById<TextView>(R.id.txtTitle2)
        val author2 = findViewById<TextView>(R.id.txtAuthor2)
        val cover3 = findViewById<ImageView>(R.id.imgCover3)
        val title3 = findViewById<TextView>(R.id.txtTitle3)
        val author3 = findViewById<TextView>(R.id.txtAuthor3)

        viewModel.allBooks.observe(this) { books ->
            if (books.isEmpty()) {
                // optional: show placeholders or empty state
                trendingTitle.text = "No books yet"
                trendingAuthor.text = ""
                trendingDesc.text = ""
                return@observe
            }

            // Trending: pick first book (or compute by rating/popularity)
            val trending = books.first()
            trendingTitle.text = trending.title
            trendingAuthor.text = trending.author
            trendingDesc.text = trending.description
            val bgRes = resources.getIdentifier(trending.coverResName, "drawable", packageName)
            if (bgRes != 0) trendingBackground.setImageResource(bgRes)

            // New releases: fill first three items if available
            fun bindCard(bookIndex: Int, cover: ImageView, t: TextView, a: TextView) {
                if (books.size > bookIndex) {
                    val b = books[bookIndex]
                    val res = resources.getIdentifier(b.coverResName, "drawable", packageName)
                    if (res != 0) cover.setImageResource(res)
                    t.text = b.title
                    a.text = b.author
                    cover.setOnClickListener {
                        val intent = Intent(this, BookDetailsActivity::class.java)
                        intent.putExtra("book_id", b.id)
                        startActivity(intent)
                    }
                } else {
                    cover.setImageResource(R.color.colorSurface)
                    t.text = ""
                    a.text = ""
                }
            }

            bindCard(0, cover1, title1, author1)
            bindCard(1, cover2, title2, author2)
            bindCard(2, cover3, title3, author3)
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_discover
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_discover -> true

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
