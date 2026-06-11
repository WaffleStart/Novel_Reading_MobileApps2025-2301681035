package com.example.novelrepo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: Int,
    val title: String,
    val author: String,
    val description: String,
    val genres: String,
    val pages: Int,
    val readTime: String,
    val coverResName: String,
    val inLibrary: Boolean = false,
    val percentRead: Int = 0,
    val currentChapter: Int = 0,
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val isNewRelease: Boolean = false
) {
    fun genreList(): List<String> = if (genres.isBlank()) emptyList() else genres.split(",").map { it.trim() }
}
