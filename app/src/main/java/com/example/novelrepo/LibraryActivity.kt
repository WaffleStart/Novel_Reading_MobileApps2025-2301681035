package com.example.novelrepo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.novelrepo.data.Book

class LibraryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerLibrary)

        // Default Android placeholder image
        val placeholder = android.R.drawable.ic_menu_gallery
        val books = listOf(
            Book("The Hobbit", "J.R.R. Tolkien", 60, R.drawable.hobbit_cover),
            Book("Dune", "Frank Herbert", 40, placeholder),
            Book("Mistborn", "Brandon Sanderson", 75, placeholder),
            Book("1984", "George Orwell", 20, placeholder),
            Book("The Witcher", "Andrzej Sapkowski", 55, placeholder),
            Book("The Name of the Wind", "Patrick Rothfuss", 90, placeholder)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookAdapter(books) { book ->
            Toast.makeText(this, "Clicked: ${book.title}", Toast.LENGTH_SHORT).show()
        }
    }
}