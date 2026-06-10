package com.example.novelrepo.util

import com.example.novelrepo.R
import com.example.novelrepo.data.Book

object SampleBooks {

    val books = listOf(

        Book(
            1,
            "The Last Light",
            "E.M. Arlen",
            "Fantasy",
            "Fantasy story",
            "Chapter 1...\n\nChapter 2...",
            R.drawable.last_light
        ),

        Book(
            2,
            "The Silent Grove",
            "M.R. Finch",
            "Mystery",
            "Mystery story",
            "Chapter 1...\n\nChapter 2...",
            R.drawable.silent_grove
        ),

        Book(
            3,
            "Whispers of Night",
            "L.D. Everett",
            "Fantasy",
            "Dark fantasy",
            "Chapter 1...\n\nChapter 2...",
            R.drawable.whispers
        )
    )
}